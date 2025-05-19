package com.micro.salaryservice.service.impl;

import com.micro.salaryservice.model.SalaryIncrement;
import com.micro.salaryservice.repository.SalaryIncrementRepository;
import com.micro.salaryservice.service.SalaryIncrementService;
import com.micro.salaryservice.validator.SalaryIncrementValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "SALARY-INCREMENT-SERVICE")
public class SalaryIncrementServiceImpl implements SalaryIncrementService {
    private final SalaryIncrementRepository salaryIncrementRepository;
    private final SalaryIncrementValidator salaryIncrementValidator;

    @Override
    public SalaryIncrement createSalaryIncrement(SalaryIncrement salaryIncrement) {
        salaryIncrementValidator.checkSalaryIncrement(salaryIncrement);
        salaryIncrement.setCreatedDate(LocalDate.now());
        log.info("Creating salary increment: {}", salaryIncrement);
        return salaryIncrementRepository.insert(salaryIncrement);

    }

    @Override
    public SalaryIncrement getSalaryIncrementById(String salaryIncrementId) {
        log.info("Getting salary increment with ID: {}", salaryIncrementId);
        salaryIncrementValidator.checkSalaryIncrementId(salaryIncrementId);
        return salaryIncrementRepository.findBySalaryIncrementId(salaryIncrementId);
    }

    @Override
    public List<SalaryIncrement> getAllSalaryIncrements() {
        return salaryIncrementRepository.findAll();
    }

    @Override
    public SalaryIncrement updateSalaryIncrement(String salaryIncrementId, SalaryIncrement salaryIncrement) {
        log.info("Updating salary increment with ID: {}", salaryIncrementId);
        salaryIncrementValidator.checkSalaryIncrementId(salaryIncrementId);
        SalaryIncrement currentSalaryIncrement = salaryIncrementRepository.findBySalaryIncrementId(salaryIncrementId);
        salaryIncrementValidator.checkSalaryIncrementToUpdate(currentSalaryIncrement, salaryIncrement);
        salaryIncrement.setCreatedDate(currentSalaryIncrement.getCreatedDate());
        return salaryIncrementRepository.save(salaryIncrement);
    }

    @Override
    public void deleteSalaryIncrement(String salaryIncrementId) {
        log.info("Deleting salary increment with ID: {}", salaryIncrementId);
        salaryIncrementValidator.checkSalaryIncrementId(salaryIncrementId);
        salaryIncrementRepository.deleteBySalaryIncrementId(salaryIncrementId);
    }

    @Override
    public List<SalaryIncrement> getSalaryIncrementsByEmployeeId(Long employeeId) {
        salaryIncrementValidator.checkValidEmployeeId(employeeId);
        log.info("Getting salary increments for employee ID: {}", employeeId);
        return salaryIncrementRepository.findByEmployeeId(employeeId);
    }
}
