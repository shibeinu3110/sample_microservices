package com.micro.departmentservice.repository;

import com.micro.departmentservice.model.Department;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class DepartmentRepository {
    List<Department> departments = new ArrayList<>();
    public Department addDepartment(Department department) {
        departments.add(department);
        return department;
    }

    public List<Department> getAllDepartments() {
        return departments;
    }

    public Department getDepartmentById(Long id) {
        return departments.stream()
                .filter(department -> department.getId().equals(id))
                .findFirst()
                .orElse(null);
    }
}
