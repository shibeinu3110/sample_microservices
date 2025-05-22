package com.micro.salaryservice.service.impl;

import com.micro.commonlib.common.exception.ErrorMessages;
import com.micro.commonlib.common.exception.StandardException;
import com.micro.commonlib.response.PageResponse;
import com.micro.salaryservice.client.EmployeeClient;
import com.micro.salaryservice.common.enumarate.Status;
import com.micro.salaryservice.dto.SalaryMailDTO;
import com.micro.salaryservice.model.Employee;
import com.micro.salaryservice.model.SalaryIncrement;
import com.micro.salaryservice.repository.SalaryIncrementRepository;
import com.micro.salaryservice.service.SalaryIncrementService;
import com.micro.salaryservice.validator.SalaryIncrementValidator;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "SALARY-INCREMENT-SERVICE")
public class SalaryIncrementServiceImpl implements SalaryIncrementService {
    private final SalaryIncrementRepository salaryIncrementRepository;
    private final SalaryIncrementValidator salaryIncrementValidator;
    private final EmployeeClient employeeClient;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    public SalaryIncrement createSalaryIncrement(SalaryIncrement salaryIncrement, HttpServletRequest request) {
        //validate the salary increment
        salaryIncrementValidator.checkSalaryIncrement(salaryIncrement);
        salaryIncrementValidator.checkValidEmployeeId(salaryIncrement.getEmployeeId());


        salaryIncrement.setCreatedDate(LocalDate.now());
        log.info("Creating salary increment: {}", salaryIncrement);
        salaryIncrement.setStatus(Status.CREATED);
        salaryIncrement.setCreatedBy(request.getHeader("username"));
        salaryIncrement.setCreatedByRole(request.getHeader("role"));
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
    public SalaryIncrement getSalaryIncrementById(String salaryIncrementId) {
        log.info("Getting salary increment with ID: {}", salaryIncrementId);
        salaryIncrementValidator.checkSalaryIncrementId(salaryIncrementId);
        return salaryIncrementRepository.findBySalaryIncrementId(salaryIncrementId);
    }

    @Override
    public PageResponse<SalaryIncrement> getAllSalaryIncrements(Pageable pageable) {
        var pageDate =  salaryIncrementRepository.findAll(pageable);

        return PageResponse.<SalaryIncrement>builder()
                .currentPage(pageDate.getNumber()+1)
                .totalPage(pageDate.getTotalPages())
                .totalElements(pageDate.getTotalElements())
                .data(pageDate.getContent())
                .build();
    }

    @Override
    public SalaryIncrement updateSalaryIncrement(String salaryIncrementId, SalaryIncrement salaryIncrement, HttpServletRequest request) {
        log.info("Updating salary increment with ID: {}", salaryIncrementId);
        salaryIncrementValidator.checkSalaryIncrementId(salaryIncrementId);
        SalaryIncrement currentSalaryIncrement = salaryIncrementRepository.findBySalaryIncrementId(salaryIncrementId);
        String name = request.getHeader("username");
        if(!name.equals(currentSalaryIncrement.getCreatedBy())) {
            throw new StandardException(ErrorMessages.ACCESS_DENIED, "You must be the creator of this salary increment to update it");
        }


        salaryIncrementValidator.checkSalaryIncrementToUpdate(currentSalaryIncrement, salaryIncrement);
//        salaryIncrement.setCreatedDate(currentSalaryIncrement.getCreatedDate());
//        salaryIncrement.setStatus(Status.UPDATED);
//        salaryIncrement.setUpdatedBy(request.getHeader("username"));
        currentSalaryIncrement.setUpdatedBy(request.getHeader("username"));
        currentSalaryIncrement.setStatus(Status.UPDATED);

        return salaryIncrementRepository.save(currentSalaryIncrement);
    }

    @Override
    public void deleteSalaryIncrement(String salaryIncrementId, HttpServletRequest request) {
        log.info("Deleting salary increment with ID: {}", salaryIncrementId);
        salaryIncrementValidator.checkSalaryIncrementId(salaryIncrementId);
        String name = request.getHeader("username");
        if(!name.equals(salaryIncrementRepository.findBySalaryIncrementId(salaryIncrementId).getCreatedBy())) {
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
}
