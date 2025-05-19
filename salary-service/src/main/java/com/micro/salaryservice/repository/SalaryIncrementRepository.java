package com.micro.salaryservice.repository;

import com.micro.salaryservice.model.SalaryIncrement;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SalaryIncrementRepository extends MongoRepository<SalaryIncrement, String> {
    SalaryIncrement findBySalaryIncrementId(String salaryIncrementId);

    void deleteBySalaryIncrementId(String salaryIncrementId);
    List<SalaryIncrement> findByEmployeeId(Long employeeId);
}
