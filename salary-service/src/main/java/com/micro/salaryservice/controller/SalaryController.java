package com.micro.salaryservice.controller;

import com.micro.commonlib.common.StandardResponse;
import com.micro.commonlib.common.enumarate.UserStatus;
import com.micro.commonlib.common.exception.ErrorMessages;
import com.micro.commonlib.common.exception.StandardException;
import com.micro.commonlib.response.PageResponse;
import com.micro.salaryservice.consts.ConstParameter;
import com.micro.salaryservice.dto.LeaderDecisionDTO;
import com.micro.salaryservice.model.SalaryIncrement;
import com.micro.salaryservice.service.SalaryIncrementService;
import com.micro.salaryservice.service.impl.ExportServiceFactory;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/salaries")
@RequiredArgsConstructor
@Slf4j(topic = "SALARY-CONTROLLER")
public class SalaryController {
    private final SalaryIncrementService salaryIncrementService;
    private final ExportServiceFactory exportServiceFactory;

    @PostMapping()
    public StandardResponse<SalaryIncrement> createSalaryIncrement(@Valid @RequestBody SalaryIncrement salaryIncrement,
                                                                   HttpServletRequest request) {
        SalaryIncrement salaryIncrement1 = null;

        if (request.getHeader("role").toString().equalsIgnoreCase(UserStatus.ROLE_MANAGER.toString())) {
            String username = request.getHeader("username");
            String role = request.getHeader("role");
            salaryIncrement1 = salaryIncrementService.createSalaryIncrement(salaryIncrement, username, role);
        } else {
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
        int pageIndex = Math.max(page - 1, 0);
        Pageable pageable = PageRequest.of(pageIndex, size);
        return StandardResponse.build(salaryIncrementService.getAllSalaryIncrements(pageable), "All salary increments retrieved successfully");
    }


    @GetMapping("/all/page-default")
    public StandardResponse<Page<SalaryIncrement>> getAllSalaryIncrementsByPage(@RequestParam(defaultValue = "0") int page,
                                                                                @RequestParam(defaultValue = "10") int size) {
        int pageIndex = Math.max(page - 1, 0);
        Pageable pageable = PageRequest.of(pageIndex, size);
        return StandardResponse.build(salaryIncrementService.getAllSalaryIncrementsByPage(pageable), "All salary increments retrieved successfully");
    }


    @PutMapping("/{salaryIncrementId}")
    public StandardResponse<SalaryIncrement> updateSalaryIncrement(@PathVariable String salaryIncrementId, @RequestBody SalaryIncrement salaryIncrement, HttpServletRequest request) {

        SalaryIncrement salaryIncrement1 = null;
        if (request.getHeader("role").toString().equalsIgnoreCase(UserStatus.ROLE_MANAGER.toString())) {
            String username = request.getHeader("username");
            String role = request.getHeader("role");
            salaryIncrement1 = salaryIncrementService.updateSalaryIncrement(salaryIncrementId, salaryIncrement, username);
        } else {
            throw new StandardException(ErrorMessages.ACCESS_DENIED, "You must have role: ROLE_MANAGER to update salary increment");
        }

        return StandardResponse.build(salaryIncrement1, "Salary increment updated successfully");
    }

    @DeleteMapping("/{salaryIncrementId}")
    public StandardResponse<String> deleteSalaryIncrement(@PathVariable String salaryIncrementId, HttpServletRequest request) {
        if (request.getHeader("role").toString().equalsIgnoreCase(UserStatus.ROLE_MANAGER.toString())) {
            String username = request.getHeader("username");
            String role = request.getHeader("role");
            salaryIncrementService.deleteSalaryIncrement(salaryIncrementId, username);
        } else {
            throw new StandardException(ErrorMessages.ACCESS_DENIED, "You must have role: ROLE_MANAGER to delete this salary increment");
        }
        return StandardResponse.build("Salary increment deleted successfully");
    }

    @GetMapping("/employee/{employeeId}")
    public StandardResponse<List<SalaryIncrement>> getSalaryIncrementsByEmployeeId(@PathVariable Long employeeId) {
        return StandardResponse.build(salaryIncrementService.getSalaryIncrementsByEmployeeId(employeeId), "Salary increments retrieved successfully");
    }


    @PutMapping("/leader-decision/{salaryIncrementId}")
    public StandardResponse<SalaryIncrement> leaderDecision(@PathVariable String salaryIncrementId,
                                                            @RequestBody LeaderDecisionDTO leaderDecisionDTO,
                                                            HttpServletRequest request) {
        if (!request.getHeader("role").toString().equalsIgnoreCase(UserStatus.ROLE_LEADER.toString())) {
            throw new StandardException(ErrorMessages.ACCESS_DENIED, "You must have role: ROLE_LEADER to make a decision");
        }

        String username = request.getHeader("username");
        String role = request.getHeader("role");
        return StandardResponse.build(salaryIncrementService.leaderDecision(salaryIncrementId, leaderDecisionDTO, username), "Leader decision made successfully");
    }


    @GetMapping("/export/{type}")
    public StandardResponse<String> exportFactory(@PathVariable("type") String type,
                                                  HttpServletResponse response) {
        response.setContentType(ConstParameter.CONTENT_TYPE);

        if (type.equalsIgnoreCase("pdf")) {
            response.setHeader(ConstParameter.KEY, ConstParameter.VALUE_PDF);
        } else if (type.equalsIgnoreCase("excel")) {
            response.setHeader(ConstParameter.KEY, ConstParameter.VALUE);
        } else {
            throw new StandardException(ErrorMessages.INVALID_FORMAT, "Input type must be excel or pdf");
        }

        log.info("Exporting salary increments to " + type.toUpperCase());
        try {
            exportServiceFactory.getCurrentService(type).export(response.getOutputStream());
        } catch (IOException e) {
            throw new StandardException(ErrorMessages.BAD_REQUEST, "Error while writing PDF file");
        }
        return StandardResponse.build("Excel file generated successfully");
    }


    @GetMapping("/check")
    public StandardResponse<String> check(@RequestHeader("username") List<String> username, @RequestHeader("role") List<String> role) {
        log.info("Username: {}", username.toString());
        log.info("Role: {}", role.toString());
        if (UserStatus.ROLE_LEADER.toString().equals(role)) {
            log.info("User is a leader");
        } else {

            log.info(UserStatus.ROLE_LEADER.toString());
        }
        return StandardResponse.build("Salary service is running");
    }


}
