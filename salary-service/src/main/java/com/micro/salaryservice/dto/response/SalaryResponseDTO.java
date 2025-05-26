package com.micro.salaryservice.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.micro.salaryservice.common.enumarate.Status;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SalaryResponseDTO implements Serializable {
    String salaryIncrementId;
    Long employeeId;
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
