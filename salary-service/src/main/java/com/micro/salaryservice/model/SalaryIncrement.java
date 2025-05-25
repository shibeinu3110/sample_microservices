package com.micro.salaryservice.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.micro.salaryservice.common.enumarate.Status;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@Builder
@Document(collection = "salary_increment")
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SalaryIncrement implements Serializable {
    @Id
    String salaryIncrementId;
    @NotNull(message = "Employee ID cannot be null")
    Long employeeId;

    @Min(value = 0, message = "Increment amount must be greater than or equal to 0")
    Long incrementAmount;


    LocalDate createdDate;

    Status status;
    String createdBy;
    String updatedBy;
    String createdByRole;

    String endBy;
    LocalDate endDate;
    String leaderNote;
}
