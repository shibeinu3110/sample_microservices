package com.micro.salaryservice.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.micro.salaryservice.common.enumarate.Status;
import jakarta.validation.constraints.Min;
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

    @Min(value = 0, message = "Increment amount must be greater than or equal to 0")
    private Long incrementAmount;


    private LocalDate createdDate;

    private Status status;
    private String createdBy;
    private String updatedBy;
    private String createdByRole;

    private String endBy;
    private LocalDate endDate;
    private String leaderNote;
}
