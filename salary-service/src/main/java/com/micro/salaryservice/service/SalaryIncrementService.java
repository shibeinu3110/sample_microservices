package com.micro.salaryservice.service;

import com.micro.salaryservice.model.SalaryIncrement;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface SalaryIncrementService {
    SalaryIncrement createSalaryIncrement(SalaryIncrement salaryIncrement, HttpServletRequest request);

    SalaryIncrement getSalaryIncrementById(String salaryIncrementId);

    List<SalaryIncrement> getAllSalaryIncrements();

    SalaryIncrement updateSalaryIncrement(String salaryIncrementId, SalaryIncrement salaryIncrement, HttpServletRequest request);

    void deleteSalaryIncrement(String salaryIncrementId, HttpServletRequest request);

    List<SalaryIncrement> getSalaryIncrementsByEmployeeId(Long employeeId);
}
