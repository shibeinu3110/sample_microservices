package com.micro.salaryservice.model;

public record Employee(Long id, Long departmentId, String firstName, String lastName, String email) {
}
