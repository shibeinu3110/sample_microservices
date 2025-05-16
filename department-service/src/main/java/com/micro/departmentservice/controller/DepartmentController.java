package com.micro.departmentservice.controller;

import com.micro.departmentservice.client.EmployeeClient;
import com.micro.departmentservice.model.Department;
import com.micro.departmentservice.repository.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/departments")
@RequiredArgsConstructor
@Slf4j

public class DepartmentController {
    private final DepartmentRepository departmentRepository;
    private final EmployeeClient employeeClient;

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @PostMapping()
    public Department addDepartment(@RequestBody Department department) {
        log.info("Inside addDepartment method of DepartmentController");
        Department department1 = departmentRepository.addDepartment(department);
        kafkaTemplate.send("test-topic", department1);
        return department1;
    }

    @GetMapping("/{id}")
    public Department getDepartmentById(@PathVariable Long id) {
        log.info("Inside getDepartmentById method of DepartmentController");
        return departmentRepository.getDepartmentById(id);
    }

    @GetMapping("/all")
    public List<Department> getAllDepartments() {
        log.info("Inside getAllDepartments method of DepartmentController");
        return departmentRepository.getAllDepartments();
    }

    @GetMapping("/with-employees")
    public List<Department> findAllWithEmployees() {
        log.info("Inside findAllWithEmployees method of DepartmentController");
        List<Department> departments = departmentRepository.getAllDepartments();
        departments.stream().forEach(department -> {
            department.setEmployees(employeeClient.getEmployeesByDepartmentId(department.getId()));
        });
        return departments;
    }
}
