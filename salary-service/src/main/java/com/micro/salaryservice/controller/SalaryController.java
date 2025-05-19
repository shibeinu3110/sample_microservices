package com.micro.salaryservice.controller;

import com.micro.salaryservice.common.StandardResponse;
import com.micro.salaryservice.model.SalaryIncrement;
import com.micro.salaryservice.service.SalaryIncrementService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/salaries")
@RequiredArgsConstructor
@Slf4j(topic = "SALARY-CONTROLLER")
public class SalaryController {
    private final SalaryIncrementService salaryIncrementService;
    @PostMapping()
    public StandardResponse<SalaryIncrement> createSalaryIncrement(@Valid @RequestBody SalaryIncrement salaryIncrement) {
        return StandardResponse.build(salaryIncrementService.createSalaryIncrement(salaryIncrement), "Salary increment created successfully");
    }

    @GetMapping("/{salaryIncrementId}")
    public StandardResponse<SalaryIncrement> getSalaryIncrement(@PathVariable String salaryIncrementId) {
        return StandardResponse.build(salaryIncrementService.getSalaryIncrementById(salaryIncrementId), "Salary increment retrieved successfully");
    }

    @GetMapping("/all")
    public StandardResponse<List<SalaryIncrement>> getAllSalaryIncrements() {
        return StandardResponse.build(salaryIncrementService.getAllSalaryIncrements(), "All salary increments retrieved successfully");
    }

    @PutMapping("/{salaryIncrementId}")
    public StandardResponse<SalaryIncrement> updateSalaryIncrement(@PathVariable String salaryIncrementId, @RequestBody SalaryIncrement salaryIncrement) {
        return StandardResponse.build(salaryIncrementService.updateSalaryIncrement(salaryIncrementId, salaryIncrement), "Salary increment updated successfully");
    }

    @DeleteMapping("/{salaryIncrementId}")
    public StandardResponse<String> deleteSalaryIncrement(@PathVariable String salaryIncrementId) {
        salaryIncrementService.deleteSalaryIncrement(salaryIncrementId);
        return StandardResponse.build("Salary increment deleted successfully");
    }

    @GetMapping("/employee/{employeeId}")
    public StandardResponse<List<SalaryIncrement>> getSalaryIncrementsByEmployeeId(@PathVariable Long employeeId) {
        return StandardResponse.build(salaryIncrementService.getSalaryIncrementsByEmployeeId(employeeId), "Salary increments retrieved successfully");
    }
}
