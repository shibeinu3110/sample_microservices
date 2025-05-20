package com.micro.salaryservice.validator;

import com.micro.commonlib.common.exception.ErrorMessages;
import com.micro.commonlib.common.exception.StandardException;
import com.micro.salaryservice.client.EmployeeClient;
import com.micro.salaryservice.model.SalaryIncrement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Component;
import org.springframework.data.mongodb.core.query.Query;

@Component
@Slf4j
@RequiredArgsConstructor
public class SalaryIncrementValidator {
    private final MongoTemplate mongoTemplate;
    private final EmployeeClient employeeClient;

    public void checkSalaryIncrement(SalaryIncrement salaryIncrement) {
        log.info("Validating salary increment: {}", salaryIncrement);
        // Check if the employee ID is valid
        checkValidEmployeeId(salaryIncrement.getEmployeeId());
        checkDuplicateSalaryIncrementId(salaryIncrement.getSalaryIncrementId());
    }

    public void checkSalaryIncrementToUpdate(SalaryIncrement currentSalaryIncrement, SalaryIncrement newSalaryIncrement) {
        log.info("Validating current salary increment: {}", currentSalaryIncrement);
        log.info("Validating salary increment to update: {}", newSalaryIncrement);

        // Check if the salary increment ID is valid
        if(!newSalaryIncrement.getSalaryIncrementId().equals(currentSalaryIncrement.getSalaryIncrementId())) {
            throw new StandardException(ErrorMessages.SAVE_DATABASE_ERROR, "Can't change salary increment ID when updating");
        }
    }

    private void checkDuplicateSalaryIncrementId(String salaryIncrementId) {
        boolean exists = mongoTemplate.exists(
                new Query(Criteria.where("salaryIncrementId").is(salaryIncrementId)),
                SalaryIncrement.class
        );
        if(exists) {
            throw new StandardException(ErrorMessages.DUPLICATE, "Salary Increment ID already exists");
        }
    }

    public void checkValidEmployeeId(Long employeeId) {
        try {
            employeeClient.getEmployeeById(employeeId);
        } catch (Exception e) {
            throw new StandardException(ErrorMessages.NOT_FOUND, "Employee ID not found");
        }
    }


    public void checkSalaryIncrementId(String salaryIncrementId) {
        log.info("Validating salary increment ID: {}", salaryIncrementId);
        if(salaryIncrementId == null || salaryIncrementId.isEmpty()) {
            throw new StandardException(ErrorMessages.INVALID_VALUE, "Salary Increment ID cannot be null or empty");
        }
        boolean exists = mongoTemplate.exists(
                new Query(Criteria.where("salaryIncrementId").is(salaryIncrementId)),
                SalaryIncrement.class
        );
        if(!exists) {
            throw new StandardException(ErrorMessages.NOT_FOUND, "Salary Increment ID not found");
        }
    }

}
