package com.micro.employeeservice.controller;

import com.micro.employeeservice.model.Employee;
import com.micro.employeeservice.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employees")
@RequiredArgsConstructor
@Slf4j
public class EmployeeController {
    private final EmployeeRepository employeeRepository;
    @PostMapping()
    public Employee addEmployee(@RequestBody Employee employee) {
        log.info("Adding employee: {}", employee);
        return employeeRepository.addEmployee(employee);
    }

    @GetMapping("/all")
    public List<Employee> getAllEmployees() {
        log.info("Fetching all employees");
        return employeeRepository.getAllEmployees();
    }

    @GetMapping("/{id}")
    public Employee getEmployeeById(@PathVariable Long id) {
        log.info("Fetching employee with id: {}", id);
        return employeeRepository.getEmployeeById(id);
    }
    @GetMapping("/department/{departmentId}")
    public List<Employee> getEmployeesByDepartmentId(@PathVariable Long departmentId) {
        log.info("Fetching employees with department id: {}", departmentId);
        return employeeRepository.getEmployeesByDepartmentId(departmentId);
    }
}
