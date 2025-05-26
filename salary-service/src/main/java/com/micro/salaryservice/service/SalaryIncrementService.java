package com.micro.salaryservice.service;

import com.micro.commonlib.response.PageResponse;
import com.micro.salaryservice.dto.LeaderDecisionDTO;
import com.micro.salaryservice.dto.request.SalaryRequestDTO;
import com.micro.salaryservice.dto.response.SalaryResponseDTO;
import com.micro.salaryservice.model.SalaryIncrement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SalaryIncrementService {
    SalaryResponseDTO createSalaryIncrement(SalaryRequestDTO salaryIncrement, String username, String role);

    SalaryResponseDTO getSalaryIncrementById(String salaryIncrementId);

    PageResponse<SalaryResponseDTO> getAllSalaryIncrements(Pageable pageable);

    SalaryResponseDTO updateSalaryIncrement(String salaryIncrementId, SalaryRequestDTO salaryIncrement, String username);

    void deleteSalaryIncrement(String salaryIncrementId, String username);

    List<SalaryResponseDTO> getSalaryIncrementsByEmployeeId(Long employeeId);

    SalaryResponseDTO leaderDecision(String salaryIncrementId, LeaderDecisionDTO leaderDecisionDTO, String username);

    Page<SalaryIncrement> getAllSalaryIncrementsByPage(Pageable pageable);
}
