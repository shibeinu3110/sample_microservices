package com.micro.employeeservice.dto;

import com.micro.employeeservice.model.Employee;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class DepartmentDTO {
    private Long id;
    private String name;
    private List<Employee> employees = new ArrayList<>();
}
