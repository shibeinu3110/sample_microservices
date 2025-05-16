package com.micro.departmentservice.model;

public record Employee(Long id, Long departmentId, String name, String email) {
}
