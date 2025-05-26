package com.micro.employeeservice.service;

import com.micro.employeeservice.dto.request.EmployeeRequestDTO;
import com.micro.employeeservice.dto.response.EmployeeResponseDTO;
import com.micro.employeeservice.model.Employee;

import java.util.List;

public interface EmployeeService {
    EmployeeResponseDTO saveEmployee(EmployeeRequestDTO employee);
    EmployeeResponseDTO getEmployeeById(Long employeeId);

    List<EmployeeResponseDTO> getAllEmployees();

    EmployeeResponseDTO updateEmployee(Long employeeId, EmployeeRequestDTO newEmployee);

    void deleteEmployee(Long employeeId);
}
