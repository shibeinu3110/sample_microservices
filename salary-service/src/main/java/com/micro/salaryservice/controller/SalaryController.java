package com.micro.salaryservice.controller;

import com.micro.commonlib.common.StandardResponse;

import com.micro.commonlib.common.enumarate.UserStatus;
import com.micro.commonlib.common.exception.ErrorMessages;
import com.micro.commonlib.common.exception.StandardException;
import com.micro.commonlib.response.PageResponse;
import com.micro.salaryservice.dto.LeaderDecisionDTO;
import com.micro.salaryservice.model.SalaryIncrement;
import com.micro.salaryservice.service.ExcelService;
import com.micro.salaryservice.service.SalaryIncrementService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/salaries")
@RequiredArgsConstructor
@Slf4j(topic = "SALARY-CONTROLLER")
public class SalaryController {
    private final SalaryIncrementService salaryIncrementService;
    private final ExcelService excelService;
    @PostMapping()
    public StandardResponse<SalaryIncrement> createSalaryIncrement(@Valid @RequestBody SalaryIncrement salaryIncrement,
                                                                   HttpServletRequest request) {
        SalaryIncrement salaryIncrement1 = null;

        if(request.getHeader("role").toString().equalsIgnoreCase(UserStatus.ROLE_MANAGER.toString())) {
            salaryIncrement1 = salaryIncrementService.createSalaryIncrement(salaryIncrement, request);
        }
        else {
            throw new StandardException(ErrorMessages.ACCESS_DENIED, "You must have role: ROLE_MANAGER to create salary increment");
        }
        return StandardResponse.build(salaryIncrement1, "Salary increment created successfully");
    }

    @GetMapping("/{salaryIncrementId}")
    public StandardResponse<SalaryIncrement> getSalaryIncrement(@PathVariable String salaryIncrementId) {

        return StandardResponse.build(salaryIncrementService.getSalaryIncrementById(salaryIncrementId), "Salary increment retrieved successfully");
    }

    @GetMapping("/all")
    public StandardResponse<PageResponse<SalaryIncrement>> getAllSalaryIncrements(@RequestParam(defaultValue = "0") int page,
                                                                          @RequestParam(defaultValue = "10") int size) {
        int pageIndex = Math.max(page-1,0);
        Pageable pageable = PageRequest.of(pageIndex, size);
        return StandardResponse.build(salaryIncrementService.getAllSalaryIncrements(pageable), "All salary increments retrieved successfully");
    }

    @PutMapping("/{salaryIncrementId}")
    public StandardResponse<SalaryIncrement> updateSalaryIncrement(@PathVariable String salaryIncrementId, @RequestBody SalaryIncrement salaryIncrement, HttpServletRequest request) {

        SalaryIncrement salaryIncrement1 = null;
        if(request.getHeader("role").toString().equalsIgnoreCase(UserStatus.ROLE_MANAGER.toString())) {
            salaryIncrement1 = salaryIncrementService.updateSalaryIncrement(salaryIncrementId, salaryIncrement, request);
        }
        else {
            throw new StandardException(ErrorMessages.ACCESS_DENIED, "You must have role: ROLE_MANAGER to update salary increment");
        }

        return StandardResponse.build(salaryIncrement1, "Salary increment updated successfully");
    }

    @DeleteMapping("/{salaryIncrementId}")
    public StandardResponse<String> deleteSalaryIncrement(@PathVariable String salaryIncrementId, HttpServletRequest request) {
        if(request.getHeader("role").toString().equalsIgnoreCase(UserStatus.ROLE_MANAGER.toString())) {
            salaryIncrementService.deleteSalaryIncrement(salaryIncrementId, request);
        }
        else {
            throw new StandardException(ErrorMessages.ACCESS_DENIED, "You must have role: ROLE_MANAGER to delete this salary increment");
        }
        return StandardResponse.build("Salary increment deleted successfully");
    }

    @GetMapping("/employee/{employeeId}")
    public StandardResponse<List<SalaryIncrement>> getSalaryIncrementsByEmployeeId(@PathVariable Long employeeId) {
        return StandardResponse.build(salaryIncrementService.getSalaryIncrementsByEmployeeId(employeeId), "Salary increments retrieved successfully");
    }

    @GetMapping("/check")
    public StandardResponse<String> check(@RequestHeader("username") String username, @RequestHeader("role") String role) {
        log.info("Username: {}", username);
        log.info("Role: {}", role);
        if(UserStatus.ROLE_LEADER.toString().equals(role)) {
            log.info("User is a leader");
        } else {
            log.info(role);
            log.info(UserStatus.ROLE_LEADER.toString());
        }
        return StandardResponse.build("Salary service is running");
    }

    @PutMapping("/leader-decision/{salaryIncrementId}")
    public StandardResponse<SalaryIncrement> leaderDecision(@PathVariable String salaryIncrementId,
                                                            @RequestBody LeaderDecisionDTO leaderDecisionDTO,
                                                            HttpServletRequest request) {
        if(!request.getHeader("role").toString().equalsIgnoreCase(UserStatus.ROLE_LEADER.toString())) {
            throw new StandardException(ErrorMessages.ACCESS_DENIED, "You must have role: ROLE_LEADER to make a decision");
        }

        return StandardResponse.build(salaryIncrementService.leaderDecision(salaryIncrementId, leaderDecisionDTO, request), "Leader decision made successfully");
    }

    @GetMapping("/excel")
    public StandardResponse<String> exportSalaryIncrementExcel(HttpServletResponse response) {
        log.info("Exporting salary increments to Excel");
        Object object = excelService.exportExcelFile(response);
        // Implement the logic to export salary increments to Excel
        return StandardResponse.build("Excel file generated successfully");
    }


}
