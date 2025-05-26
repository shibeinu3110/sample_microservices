package com.micro.employeeservice.dto;

import com.micro.employeeservice.model.Employee;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DepartmentDTO {
    Long id;
    String name;
    List<Employee> employees = new ArrayList<>();
}
