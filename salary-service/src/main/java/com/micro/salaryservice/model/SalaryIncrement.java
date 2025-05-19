package com.micro.salaryservice.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.Date;

@Data
@Builder
@Document(collection = "salary_increment")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SalaryIncrement {
    @Id
    private String salaryIncrementId;
    @NotNull(message = "Employee ID cannot be null")
    private Long employeeId;
    private Long incrementAmount;


    private LocalDate createdDate;
}
