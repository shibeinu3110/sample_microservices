package com.micro.salaryservice.service;

import com.micro.salaryservice.model.SalaryIncrement;

import java.util.List;

public interface SalaryIncrementService {
    SalaryIncrement createSalaryIncrement(SalaryIncrement salaryIncrement);

    SalaryIncrement getSalaryIncrementById(String salaryIncrementId);

    List<SalaryIncrement> getAllSalaryIncrements();

    SalaryIncrement updateSalaryIncrement(String salaryIncrementId, SalaryIncrement salaryIncrement);

    void deleteSalaryIncrement(String salaryIncrementId);

    List<SalaryIncrement> getSalaryIncrementsByEmployeeId(Long employeeId);
}
