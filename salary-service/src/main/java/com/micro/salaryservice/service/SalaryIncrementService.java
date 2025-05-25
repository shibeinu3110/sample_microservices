package com.micro.salaryservice.service;

import com.micro.commonlib.response.PageResponse;
import com.micro.salaryservice.dto.LeaderDecisionDTO;
import com.micro.salaryservice.model.SalaryIncrement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SalaryIncrementService {
    SalaryIncrement createSalaryIncrement(SalaryIncrement salaryIncrement, String username, String role);

    SalaryIncrement getSalaryIncrementById(String salaryIncrementId);

    PageResponse<SalaryIncrement> getAllSalaryIncrements(Pageable pageable);

    SalaryIncrement updateSalaryIncrement(String salaryIncrementId, SalaryIncrement salaryIncrement, String username);

    void deleteSalaryIncrement(String salaryIncrementId, String username);

    List<SalaryIncrement> getSalaryIncrementsByEmployeeId(Long employeeId);

    SalaryIncrement leaderDecision(String salaryIncrementId, LeaderDecisionDTO leaderDecisionDTO, String username);

    Page<SalaryIncrement> getAllSalaryIncrementsByPage(Pageable pageable);
}
