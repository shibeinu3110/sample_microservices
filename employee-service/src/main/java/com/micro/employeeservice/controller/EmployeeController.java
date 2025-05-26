package com.micro.employeeservice.controller;

import com.micro.commonlib.common.StandardResponse;
import com.micro.employeeservice.dto.request.EmployeeRequestDTO;
import com.micro.employeeservice.dto.response.EmployeeResponseDTO;
import com.micro.employeeservice.model.Employee;
import com.micro.employeeservice.service.EmployeeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employees")
@RequiredArgsConstructor
@Slf4j(topic = "EMPLOYEE-CONTROLLER")
public class EmployeeController {
    private final EmployeeService employeeService;

    @PostMapping()
    public StandardResponse<EmployeeResponseDTO> addEmployee(@Valid @RequestBody EmployeeRequestDTO employee) {
        log.info("Adding employee: {}", employee);
        return StandardResponse.build(employeeService.saveEmployee(employee), "Employee added successfully");
    }

    @GetMapping("/{employeeId}")
    public StandardResponse<EmployeeResponseDTO> getEmployeeById(@PathVariable Long employeeId) {
        log.info("Getting employee with id: {}", employeeId);
        return StandardResponse.build(employeeService.getEmployeeById(employeeId), "Employee retrieved successfully");
    }

    @GetMapping("/all")
    public StandardResponse<List<EmployeeResponseDTO>> getAllEmployees() {
        log.info("Getting all employees");
        return StandardResponse.build(employeeService.getAllEmployees(), "All employees retrieved successfully");
    }

    @PutMapping("/{employeeId}")
    public StandardResponse<EmployeeResponseDTO> updateEmployee(@PathVariable Long employeeId, @Valid @RequestBody EmployeeRequestDTO newEmployee) {
        log.info("Updating employee with id: {}", employeeId);
        return StandardResponse.build(employeeService.updateEmployee(employeeId, newEmployee), "Employee updated successfully");
    }

    @DeleteMapping("/{employeeId}")
    public StandardResponse<String> deleteEmployee(@PathVariable Long employeeId) {
        log.info("Deleting employee with id: {}", employeeId);
        employeeService.deleteEmployee(employeeId);
        return StandardResponse.build("Employee deleted successfully");
    }

    @PutMapping("/salary-acceptance/{employeeId}")
    public StandardResponse<String> acceptSalaryIncrement(@PathVariable Long employeeId, @RequestParam Long amount) {
        log.info("Accepting salary increment for employee with id: {}", employeeId);
        employeeService.acceptSalaryIncrement(employeeId, amount);
        return StandardResponse.build("Salary increment accepted successfully, you can check by getting employee by id");
    }
}
