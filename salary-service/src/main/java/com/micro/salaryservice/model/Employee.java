package com.micro.salaryservice.model;

import lombok.Getter;


public record Employee(Long id, Long departmentId, String firstName, String lastName, String email, Long currentSalary) {
}
