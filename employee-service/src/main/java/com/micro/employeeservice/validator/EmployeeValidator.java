package com.micro.employeeservice.validator;


import com.micro.commonlib.common.exception.ErrorMessages;
import com.micro.commonlib.common.exception.StandardException;
import com.micro.employeeservice.model.Employee;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

@Component
@Slf4j(topic = "EMPLOYEE-VALIDATOR")
@RequiredArgsConstructor
public class EmployeeValidator {
    private final MongoTemplate mongoTemplate;

    public void checkEmployee(Employee employee) {
        log.info("Validating employee: {}", employee);
        checkDuplicatePostcode(employee.getPostcode());
        checkDuplicateEmployeeId(employee.getEmployeeId());
        checkDuplicatePhone(employee.getPhone());
        checkDuplicateEmail(employee.getEmail());
    }


    public void checkEmployeeToUpdate(Employee currentEmployee, Employee newEmployee) {
        log.info("Validating current employee: {}", currentEmployee);
        log.info("Validating employee to update: {}", newEmployee);
        // Check if the employee ID is valid
        if (!newEmployee.getEmployeeId().equals(currentEmployee.getEmployeeId())) {
            throw new StandardException(ErrorMessages.SAVE_DATABASE_ERROR, "Can't change employee ID when updating");
        }
        if (!currentEmployee.getPhone().equals(newEmployee.getPhone())) {
            checkDuplicatePhone(newEmployee.getPhone());
        }
        if (!currentEmployee.getEmail().equals(newEmployee.getEmail())) {
            checkDuplicateEmail(newEmployee.getEmail());
        }
        // Check if the employee phone number and email already exists
        if (!currentEmployee.getPostcode().equals(newEmployee.getPostcode())) {
            checkDuplicatePostcode(newEmployee.getPostcode());
        }
    }

    private void checkDuplicateEmail(String email) {
        log.info("Checking duplicate email: {}", email);
        boolean exitsEmail = mongoTemplate.exists(
                new Query(Criteria.where("email").is(email)),
                Employee.class
        );
        if (exitsEmail) {
            throw new StandardException(ErrorMessages.DUPLICATE, "Email already exists");
        }
    }


    private void checkDuplicatePhone(String phone) {
        log.info("Checking duplicate phone: {}", phone);
        boolean exitsPostcode = mongoTemplate.exists(
                new Query(Criteria.where("phone").is(phone)),
                Employee.class
        );
        if (exitsPostcode) {
            throw new StandardException(ErrorMessages.DUPLICATE, "Phone number already exists");
        }
    }

    private void checkDuplicatePostcode(String postcode) {
        log.info("Checking duplicate postcode: {}", postcode);
        boolean exitsPostcode = mongoTemplate.exists(
                new Query(Criteria.where("postcode").is(postcode)),
                Employee.class
        );
        if (exitsPostcode) {
            throw new StandardException(ErrorMessages.DUPLICATE, "Postcode already exists");
        }
    }

    private void checkDuplicateEmployeeId(Long employeeId) {
        log.info("Checking employee id: {}", employeeId);
        boolean exitsEmployeeId = mongoTemplate.exists(
                new Query(Criteria.where("employeeId").is(employeeId)),
                Employee.class
        );
        if (exitsEmployeeId) {
            throw new StandardException(ErrorMessages.DUPLICATE, "Employee id already exists");
        }
    }


    public void checkEmployeeId(Long employeeId) {
        log.info("Checking employee id: {}", employeeId);
        boolean exitsEmployeeId = mongoTemplate.exists(
                new Query(Criteria.where("employeeId").is(employeeId)),
                Employee.class
        );
        if (!exitsEmployeeId) {
            throw new StandardException(ErrorMessages.NOT_FOUND, "Can't find employee with id: " + employeeId);
        }
    }

}
