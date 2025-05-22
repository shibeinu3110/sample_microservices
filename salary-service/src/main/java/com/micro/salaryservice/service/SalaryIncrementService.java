package com.micro.salaryservice.service;

import com.micro.commonlib.response.PageResponse;
import com.micro.salaryservice.model.SalaryIncrement;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SalaryIncrementService {
    SalaryIncrement createSalaryIncrement(SalaryIncrement salaryIncrement, HttpServletRequest request);

    SalaryIncrement getSalaryIncrementById(String salaryIncrementId);

    PageResponse<SalaryIncrement> getAllSalaryIncrements(Pageable pageable);

    SalaryIncrement updateSalaryIncrement(String salaryIncrementId, SalaryIncrement salaryIncrement, HttpServletRequest request);

    void deleteSalaryIncrement(String salaryIncrementId, HttpServletRequest request);

    List<SalaryIncrement> getSalaryIncrementsByEmployeeId(Long employeeId);
}
