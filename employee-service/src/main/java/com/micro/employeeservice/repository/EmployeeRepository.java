package com.micro.employeeservice.repository;

import com.micro.employeeservice.model.Employee;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends MongoRepository<Employee, Long> {
    Employee findByEmployeeId(Long employeeId);
    void deleteEmployeeByEmployeeId(Long employeeId);
}
