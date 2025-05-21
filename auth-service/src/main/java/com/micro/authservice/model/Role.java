package com.micro.authservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "roles")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Role {
    @Id
    private String id;

    private String role; // ví dụ: ROLE_ADMIN, ROLE_USER
}