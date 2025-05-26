package com.micro.employeeservice.dto.response;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EmployeeResponseDTO {
    Long employeeId;
    String fullName;
    Integer age;
    String postcode;
    String email;
    String phone;
    Long currentSalary;
}
