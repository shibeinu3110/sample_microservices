package com.micro.employeeservice.service.impl;

import com.micro.commonlib.common.exception.ErrorMessages;
import com.micro.commonlib.common.exception.StandardException;
import com.micro.employeeservice.dto.request.EmployeeRequestDTO;
import com.micro.employeeservice.dto.response.EmployeeResponseDTO;
import com.micro.employeeservice.mapper.EmployeeMapper;
import com.micro.employeeservice.model.Employee;
import com.micro.employeeservice.repository.EmployeeRepository;
import com.micro.employeeservice.service.EmployeeService;
import com.micro.employeeservice.validator.EmployeeValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "EMPLOYEE-SERVICE")
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeValidator employeeValidator;
    private final EmployeeRepository employeeRepository;
    private final MongoTemplate mongoTemplate;
    private final EmployeeMapper employeeMapper;

    @Override
    public EmployeeResponseDTO saveEmployee(EmployeeRequestDTO employee) {
        log.info("Saving employee: {}", employee);
        employeeValidator.checkEmployee(employeeMapper.toEmployee(employee));
        Employee employee1 = employeeRepository.save(employeeMapper.toEmployee(employee));

        return employeeMapper.toEmployeeResponseDTO(employee1);
    }

    @Override
    public EmployeeResponseDTO getEmployeeById(Long employeeId) {
        log.info("Getting employee with id: {}", employeeId);
        employeeValidator.checkEmployeeId(employeeId);
        return employeeMapper.toEmployeeResponseDTO(employeeRepository.findByEmployeeId(employeeId));
    }

    @Override
    public List<EmployeeResponseDTO> getAllEmployees() {
        log.info("Getting all employees");
        return employeeMapper.toEmployeeResponseDTOList(employeeRepository.findAll());
    }

    @Override
    public EmployeeResponseDTO updateEmployee(Long employeeId, EmployeeRequestDTO newEmployee) {
        log.info("Updating employee with id: {}", employeeId);
        employeeValidator.checkEmployeeId(employeeId);
        Employee currentEmployee = employeeRepository.findByEmployeeId(employeeId);
        employeeValidator.checkEmployeeToUpdate(currentEmployee, employeeMapper.toEmployee(newEmployee));
        Employee employee = mongoTemplate.save(employeeMapper.toEmployee(newEmployee));

        return employeeMapper.toEmployeeResponseDTO(employee);
    }

    @Override
    public void deleteEmployee(Long employeeId) {
        log.info("Deleting employee with id: {}", employeeId);
        employeeValidator.checkEmployeeId(employeeId);
        employeeRepository.deleteEmployeeByEmployeeId(employeeId);
    }

    @Override
    public void acceptSalaryIncrement(Long employeeId, Long amount) {
        log.info("Accepting salary increment for employee with id: {}", employeeId);
        employeeValidator.checkEmployeeId(employeeId);
        Employee employee = employeeRepository.findByEmployeeId(employeeId);
        Long currentSalary = employee.getCurrentSalary();
        if (currentSalary == null) {
            currentSalary = 0L; // Default to 0 if current salary is null
        }
        if(amount >= 0.2* currentSalary) {
            throw new StandardException(ErrorMessages.SAVE_DATABASE_ERROR, "Increment amount must be less than 20% of the current salary");
        }
        employee.setCurrentSalary(currentSalary + amount);
        employeeRepository.save(employee);

        log.info("Salary increment accepted for employee with id: {}", employeeId);
    }
}
