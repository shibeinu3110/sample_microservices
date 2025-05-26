package com.micro.salaryservice.client;

import com.micro.commonlib.common.StandardResponse;
import com.micro.salaryservice.model.Employee;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PutExchange;

@HttpExchange
public interface EmployeeClient {

    //maybe need to change the return type to EmployeeResponseDTO gonna check it later
    @GetExchange("/employees/{employeeId}")
    StandardResponse<Employee> getEmployeeById(@PathVariable Long employeeId);

    @PutExchange("/employees/salary-acceptance/{employeeId}")
    StandardResponse<String> acceptSalaryIncrement(@PathVariable Long employeeId, @RequestParam Long amount);


}
