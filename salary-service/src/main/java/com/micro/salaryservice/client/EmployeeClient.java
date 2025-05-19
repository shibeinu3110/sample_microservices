package com.micro.salaryservice.client;

import com.micro.salaryservice.common.StandardResponse;
import com.micro.salaryservice.model.Employee;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

@HttpExchange
public interface EmployeeClient {
    @GetExchange("/employees/{employeeId}")
    StandardResponse<Employee> getEmployeeById(@PathVariable Long employeeId);
}
