package com.micro.authservice.initializer;

import com.micro.authservice.model.Role;
import com.micro.authservice.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j(topic = "DATA-INITIALIZER-AUTH-SERVICE")
public class DataInitializer implements CommandLineRunner {
    private final RoleRepository roleRepository;
    @Override
    public void run(String... args) throws Exception {
        createRoleIfNotExist("1", "ROLE_ADMIN");
        createRoleIfNotExist("2", "ROLE_USER");
        createRoleIfNotExist("3", "ROLE_MANAGER");
    }

    private void createRoleIfNotExist(String id, String role) {
        boolean existById = roleRepository.findById(id).isPresent();
        boolean existByRole = roleRepository.findByRole(role).isPresent();
        if(!existByRole && !existById) {
            log.info("Creating role: {}", role);
            roleRepository.save(new Role(id, role));
        } else {
            log.info("Role {} already exists", role);
        }
    }
}
