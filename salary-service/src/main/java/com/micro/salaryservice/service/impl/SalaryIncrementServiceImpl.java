package com.micro.salaryservice.service.impl;

import com.micro.commonlib.common.exception.ErrorMessages;
import com.micro.commonlib.common.exception.StandardException;
import com.micro.commonlib.response.PageResponse;
import com.micro.salaryservice.client.EmployeeClient;
import com.micro.salaryservice.common.enumarate.Status;
import com.micro.salaryservice.dto.LeaderDecisionDTO;
import com.micro.salaryservice.dto.SalaryMailDTO;
import com.micro.salaryservice.dto.request.SalaryRequestDTO;
import com.micro.salaryservice.dto.response.SalaryResponseDTO;
import com.micro.salaryservice.mapper.SalaryIncrementMapper;
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
    private final SalaryIncrementMapper salaryIncrementMapper;

    @Override
    public SalaryResponseDTO createSalaryIncrement(SalaryRequestDTO salaryIncrementDTO, String username, String role) {
        SalaryIncrement salaryIncrement = salaryIncrementMapper.toSalaryIncrement(salaryIncrementDTO);
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
        return salaryIncrementMapper.toSalaryResponseDTO(savedSalary);

    }

    @Override
    @Cacheable(value = "salary", key = "#salaryIncrementId")
    public SalaryResponseDTO getSalaryIncrementById(String salaryIncrementId) {
        log.info("Getting salary increment with ID: {}", salaryIncrementId);
        salaryIncrementValidator.checkSalaryIncrementId(salaryIncrementId);

        return salaryIncrementMapper.toSalaryResponseDTO(salaryIncrementRepository.findBySalaryIncrementId(salaryIncrementId));

    }

    @Override
    public PageResponse<SalaryResponseDTO> getAllSalaryIncrements(Pageable pageable) {
        var pageData = salaryIncrementRepository.findAll(pageable);

        return PageResponse.<SalaryResponseDTO>builder()
                .currentPage(pageData.getNumber() + 1)
                .totalPage(pageData.getTotalPages())
                .totalElements(pageData.getTotalElements())
                .pageSize(pageable.getPageSize())
                .data(salaryIncrementMapper.toSalaryResponseDTOList(pageData.getContent()))
                .build();
    }

    @Override
    @CachePut(value = "salary", key = "#salaryIncrementId")
    public SalaryResponseDTO updateSalaryIncrement(String salaryIncrementId, SalaryRequestDTO salaryIncrementDTO, String username) {
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

        SalaryIncrement salaryIncrement = salaryIncrementMapper.toSalaryIncrement(salaryIncrementDTO);
        salaryIncrementValidator.checkSalaryIncrementToUpdate(currentSalaryIncrement, salaryIncrement);
        currentSalaryIncrement.setUpdatedBy(username);
        currentSalaryIncrement.setStatus(Status.UPDATED);
        currentSalaryIncrement.setIncrementAmount(salaryIncrement.getIncrementAmount());

        return salaryIncrementMapper.toSalaryResponseDTO(salaryIncrementRepository.save(currentSalaryIncrement));
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
    public List<SalaryResponseDTO> getSalaryIncrementsByEmployeeId(Long employeeId) {
        salaryIncrementValidator.checkValidEmployeeId(employeeId);
        log.info("Getting salary increments for employee ID: {}", employeeId);
        return salaryIncrementMapper.toSalaryResponseDTOList(salaryIncrementRepository.findByEmployeeId(employeeId));
    }

    @Override
    @CachePut(value = "salary", key = "#salaryIncrementId")
    public SalaryResponseDTO leaderDecision(String salaryIncrementId, LeaderDecisionDTO leaderDecisionDTO, String username) {
//        SalaryIncrement salaryIncrement = getSalaryIncrementById(salaryIncrementId));
        SalaryResponseDTO salaryResponseDTO = getSalaryIncrementById(salaryIncrementId);
        if (salaryResponseDTO.getStatus() == null) {
            throw new StandardException(ErrorMessages.INVALID_STATUS, "Salary increment is not in a valid state to make decision");
        }
        if (!Status.isValidStatusForLeader(salaryResponseDTO.getStatus().toString())) {
            throw new StandardException(ErrorMessages.INVALID_STATUS, "Salary increment is not in a valid state to make decision");
        }

        String name = username;
        if (!Status.isValidStatusForLeaderDecision(leaderDecisionDTO.getStatus())) {
            throw new StandardException(ErrorMessages.INVALID_STATUS, "Leader can only approve or reject salary increment");
        }

        SalaryIncrement salaryIncrement = salaryIncrementMapper.toSalaryIncrement(salaryResponseDTO);

        salaryIncrement.setStatus(Status.valueOf(leaderDecisionDTO.getStatus().toUpperCase()));
        salaryIncrement.setLeaderNote(leaderDecisionDTO.getNote());
        salaryIncrement.setEndBy(name);
        salaryIncrement.setEndDate(LocalDate.now());

        //handle salary increment acceptance
        if (salaryIncrement.getStatus().equals(Status.ACCEPTED)) {
            log.info("Leader accepted salary increment with ID: {}", salaryIncrementId);
            Employee employee = employeeClient.getEmployeeById(salaryIncrement.getEmployeeId()).getData();
            if (employee.currentSalary() * 0.2 <= salaryIncrement.getIncrementAmount()) {
                // can be improved by set salary increment to another status like: REJECTED, INVALID, etc.
                throw new StandardException(ErrorMessages.SAVE_DATABASE_ERROR, "Increment amount must be less than 20% of the current salary");
            }
            // Update employee's current salary

            employeeClient.acceptSalaryIncrement(
                    salaryIncrement.getEmployeeId(),
                    salaryIncrement.getIncrementAmount()
            );
            return salaryIncrementMapper.toSalaryResponseDTO(salaryIncrementRepository.save(salaryIncrement));
        } else if (salaryIncrement.getStatus().equals(Status.REJECTED)) {
            log.info("Leader rejected salary increment with ID: {}", salaryIncrementId);
            return salaryIncrementMapper.toSalaryResponseDTO(salaryIncrementRepository.save(salaryIncrement));
        } else {
            throw new StandardException(ErrorMessages.INVALID_STATUS, "Leader can only approve or reject salary increment");
        }
    }

    @Override
    public Page<SalaryIncrement> getAllSalaryIncrementsByPage(Pageable pageable) {
        return salaryIncrementRepository.findAll(pageable);
    }
}
