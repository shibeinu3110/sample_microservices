package com.micro.salaryservice.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SalaryMailDTO {
    String salaryIncrementId;
    Long incrementAmount;
    LocalDate createdDate;
    String employeeEmail;
    String employeeName;
}
