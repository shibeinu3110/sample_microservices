package com.micro.salaryservice.service.impl;

import com.micro.commonlib.common.exception.ErrorMessages;
import com.micro.commonlib.common.exception.StandardException;
import com.micro.commonlib.response.PageResponse;
import com.micro.salaryservice.client.EmployeeClient;
import com.micro.salaryservice.common.enumarate.Status;
import com.micro.salaryservice.dto.LeaderDecisionDTO;
import com.micro.salaryservice.dto.SalaryMailDTO;
import com.micro.salaryservice.model.Employee;
import com.micro.salaryservice.model.SalaryIncrement;
import com.micro.salaryservice.repository.SalaryIncrementRepository;
import com.micro.salaryservice.service.SalaryIncrementService;
import com.micro.salaryservice.validator.SalaryIncrementValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "SALARY-INCREMENT-SERVICE")
public class SalaryIncrementServiceImpl implements SalaryIncrementService {
    private final SalaryIncrementRepository salaryIncrementRepository;
    private final SalaryIncrementValidator salaryIncrementValidator;
    private final EmployeeClient employeeClient;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final RedisTemplate<String, SalaryIncrement> redisTemplate;

    @Override
    public SalaryIncrement createSalaryIncrement(SalaryIncrement salaryIncrement, String username, String role) {
        //validate the salary increment
        salaryIncrementValidator.checkSalaryIncrement(salaryIncrement);
        salaryIncrementValidator.checkValidEmployeeId(salaryIncrement.getEmployeeId());


        salaryIncrement.setCreatedDate(LocalDate.now());
        log.info("Creating salary increment: {}", salaryIncrement);
        salaryIncrement.setStatus(Status.CREATED);
        salaryIncrement.setCreatedBy(username);
        salaryIncrement.setCreatedByRole(role);
        SalaryIncrement savedSalary = salaryIncrementRepository.insert(salaryIncrement);

        //get the employee by employee id
        Employee employee = employeeClient.getEmployeeById(salaryIncrement.getEmployeeId()).getData();

        //mail service
        SalaryMailDTO salaryMailDTO = new SalaryMailDTO();
        salaryMailDTO.setSalaryIncrementId(savedSalary.getSalaryIncrementId());
        salaryMailDTO.setIncrementAmount(savedSalary.getIncrementAmount());
        salaryMailDTO.setCreatedDate(savedSalary.getCreatedDate());
        salaryMailDTO.setEmployeeName(employee.firstName() + " " + employee.lastName());
        salaryMailDTO.setEmployeeEmail(employee.email());

        kafkaTemplate.send("email-topic", salaryMailDTO);
        return savedSalary;

    }

    @Override
    @Cacheable(value = "salary", key = "#salaryIncrementId")
    public SalaryIncrement getSalaryIncrementById(String salaryIncrementId) {
        log.info("Getting salary increment with ID: {}", salaryIncrementId);
        salaryIncrementValidator.checkSalaryIncrementId(salaryIncrementId);

        return salaryIncrementRepository.findBySalaryIncrementId(salaryIncrementId);

    }

    @Override
    public PageResponse<SalaryIncrement> getAllSalaryIncrements(Pageable pageable) {
        var pageData = salaryIncrementRepository.findAll(pageable);

        return PageResponse.<SalaryIncrement>builder()
                .currentPage(pageData.getNumber() + 1)
                .totalPage(pageData.getTotalPages())
                .totalElements(pageData.getTotalElements())
                .pageSize(pageable.getPageSize())
                .data(pageData.getContent())
                .build();
    }

    @Override
    @CachePut(value = "salary", key = "#salaryIncrementId")
    public SalaryIncrement updateSalaryIncrement(String salaryIncrementId, SalaryIncrement salaryIncrement, String username) {
        log.info("Updating salary increment with ID: {}", salaryIncrementId);
        salaryIncrementValidator.checkSalaryIncrementId(salaryIncrementId);
        SalaryIncrement currentSalaryIncrement = salaryIncrementRepository.findBySalaryIncrementId(salaryIncrementId);
        if (currentSalaryIncrement.getStatus() == null) {
            throw new StandardException(ErrorMessages.INVALID_STATUS, "Salary increment is not in a valid state to be updated");
        }

        if (!Status.isValidToUpdate(currentSalaryIncrement.getStatus().toString())) {
            throw new StandardException(ErrorMessages.INVALID_STATUS, "Salary increment is not in a valid state to be updated");
        }

        String name = username;
        if (!name.equals(currentSalaryIncrement.getCreatedBy())) {
            throw new StandardException(ErrorMessages.ACCESS_DENIED, "You must be the creator of this salary increment to update it");
        }


        salaryIncrementValidator.checkSalaryIncrementToUpdate(currentSalaryIncrement, salaryIncrement);
        currentSalaryIncrement.setUpdatedBy(username);
        currentSalaryIncrement.setStatus(Status.UPDATED);
        currentSalaryIncrement.setIncrementAmount(salaryIncrement.getIncrementAmount());

        return salaryIncrementRepository.save(currentSalaryIncrement);
    }

    @Override
    @CacheEvict(value = "salary", key = "#salaryIncrementId")
    public void deleteSalaryIncrement(String salaryIncrementId, String username) {
        log.info("Deleting salary increment with ID: {}", salaryIncrementId);
        salaryIncrementValidator.checkSalaryIncrementId(salaryIncrementId);
        String name = username;
        if (!name.equals(salaryIncrementRepository.findBySalaryIncrementId(salaryIncrementId).getCreatedBy())) {
            throw new StandardException(ErrorMessages.ACCESS_DENIED, "You must be the creator of this salary increment to delete it");
        }
        salaryIncrementRepository.deleteBySalaryIncrementId(salaryIncrementId);
    }

    @Override
    public List<SalaryIncrement> getSalaryIncrementsByEmployeeId(Long employeeId) {
        salaryIncrementValidator.checkValidEmployeeId(employeeId);
        log.info("Getting salary increments for employee ID: {}", employeeId);
        return salaryIncrementRepository.findByEmployeeId(employeeId);
    }

    @Override
    public SalaryIncrement leaderDecision(String salaryIncrementId, LeaderDecisionDTO leaderDecisionDTO, String username) {
        SalaryIncrement salaryIncrement = getSalaryIncrementById(salaryIncrementId);
        if (salaryIncrement.getStatus() == null) {
            throw new StandardException(ErrorMessages.INVALID_STATUS, "Salary increment is not in a valid state to make decision");
        }
        if (!Status.isValidStatusForLeader(salaryIncrement.getStatus().toString())) {
            throw new StandardException(ErrorMessages.INVALID_STATUS, "Salary increment is not in a valid state to make decision");
        }

        String name = username;
        if (!Status.isValidStatusForLeaderDecision(leaderDecisionDTO.getStatus())) {
            throw new StandardException(ErrorMessages.INVALID_STATUS, "Leader can only approve or reject salary increment");
        }

        salaryIncrement.setStatus(Status.valueOf(leaderDecisionDTO.getStatus().toUpperCase()));
        salaryIncrement.setLeaderNote(leaderDecisionDTO.getNote());
        salaryIncrement.setEndBy(name);
        salaryIncrement.setEndDate(LocalDate.now());

        if (redisTemplate.opsForValue().get(salaryIncrement.getSalaryIncrementId()) != null) {
            redisTemplate.opsForValue().set(salaryIncrement.getSalaryIncrementId(), salaryIncrement, 30, TimeUnit.MINUTES);
        }
        return salaryIncrementRepository.save(salaryIncrement);
    }

    @Override
    public Page<SalaryIncrement> getAllSalaryIncrementsByPage(Pageable pageable) {
        return salaryIncrementRepository.findAll(pageable);
    }
}
