package com.micro.salaryservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SalaryMailDTO {
    private String salaryIncrementId;
    private Long incrementAmount;
    private LocalDate createdDate;
    private String employeeEmail;
    private String employeeName;
}
