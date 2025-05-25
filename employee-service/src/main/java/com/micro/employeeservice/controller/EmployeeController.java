package com.micro.employeeservice.controller;

import com.micro.commonlib.common.StandardResponse;
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
    public StandardResponse<Employee> addEmployee(@Valid @RequestBody Employee employee) {
        log.info("Adding employee: {}", employee);
        return StandardResponse.build(employeeService.saveEmployee(employee), "Employee added successfully");
    }

    @GetMapping("/{employeeId}")
    public StandardResponse<Employee> getEmployeeById(@PathVariable Long employeeId) {
        log.info("Getting employee with id: {}", employeeId);
        return StandardResponse.build(employeeService.getEmployeeById(employeeId), "Employee retrieved successfully");
    }

    @GetMapping("/all")
    public StandardResponse<List<Employee>> getAllEmployees() {
        log.info("Getting all employees");
        return StandardResponse.build(employeeService.getAllEmployees(), "All employees retrieved successfully");
    }

    @PutMapping("/{employeeId}")
    public StandardResponse<Employee> updateEmployee(@PathVariable Long employeeId, @Valid @RequestBody Employee newEmployee) {
        log.info("Updating employee with id: {}", employeeId);
        return StandardResponse.build(employeeService.updateEmployee(employeeId, newEmployee), "Employee updated successfully");
    }

    @DeleteMapping("/{employeeId}")
    public StandardResponse<String> deleteEmployee(@PathVariable Long employeeId) {
        log.info("Deleting employee with id: {}", employeeId);
        employeeService.deleteEmployee(employeeId);
        return StandardResponse.build("Employee deleted successfully");
    }
}
