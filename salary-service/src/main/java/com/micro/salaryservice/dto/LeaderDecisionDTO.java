package com.micro.salaryservice.dto;

import com.micro.salaryservice.common.enumarate.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class LeaderDecisionDTO {
    private String status;
    private String note;
}
