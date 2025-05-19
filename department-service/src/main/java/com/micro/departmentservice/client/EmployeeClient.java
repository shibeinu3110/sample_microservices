package com.micro.departmentservice.client;

import com.micro.departmentservice.model.Employee;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

import java.util.List;

@HttpExchange
public interface EmployeeClient {

    //define the base URL for the employee service
    //call HTTP GET method to employees/department/{departmentId}
    //this url need to be the same as the one in EmployeeController
    @GetExchange("employees/department/{departmentId}")
    List<Employee> getEmployeesByDepartmentId(@PathVariable Long departmentId);
}