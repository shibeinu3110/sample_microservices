package com.micro.employeeservice.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EmployeeRequestDTO {
    @NotNull(message = "Employee ID cannot be null")
    Long employeeId;
    String firstName;
    String lastName;

    @NotNull(message = "Age cannot be null")
    @Min(value = 1, message = "Age must be greater than 0")
    @Max(value = 120, message = "Age must be less than 120")
    Integer age;


    String postcode;

    @NotBlank
    @Pattern(
            regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$",
            message = "Email format is invalid"
    )
    String email;

    @NotBlank
    @Pattern(
            regexp = "^\\+?[0-9]{10,15}$",
            message = "Phone number format is invalid"
    )
    String phone;
}
