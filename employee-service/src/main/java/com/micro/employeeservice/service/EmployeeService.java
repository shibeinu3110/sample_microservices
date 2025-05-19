package com.micro.employeeservice.service;

import com.micro.employeeservice.model.Employee;

import java.util.List;

public interface EmployeeService {
    Employee saveEmployee(Employee employee);
    Employee getEmployeeById(Long employeeId);

    List<Employee> getAllEmployees();

    Employee updateEmployee(Long employeeId, Employee newEmployee);

    void deleteEmployee(Long employeeId);
}
