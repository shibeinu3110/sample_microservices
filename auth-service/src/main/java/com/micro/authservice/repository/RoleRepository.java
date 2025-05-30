package com.micro.authservice.repository;

import com.micro.authservice.model.Role;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends MongoRepository<Role, String> {
    Optional<Role> findById(String id);
    Optional<Role> findByRole(String role);
}
