package com.micro.salaryservice.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SalaryRequestDTO implements Serializable {
    @NotBlank(message = "Salary increment ID cannot be blank")
    String salaryIncrementId;
    @NotNull(message = "Employee ID cannot be null")
    Long employeeId;
    @Min(value = 0, message = "Increment amount must be greater than or equal to 0")
    Long incrementAmount;
    String leaderNote;
}
