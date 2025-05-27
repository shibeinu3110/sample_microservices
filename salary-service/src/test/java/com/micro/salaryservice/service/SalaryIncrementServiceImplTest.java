package com.micro.salaryservice.service;

import com.micro.salaryservice.client.EmployeeClient;
import com.micro.salaryservice.mapper.SalaryIncrementMapper;
import com.micro.salaryservice.model.SalaryIncrement;
import com.micro.salaryservice.repository.SalaryIncrementRepository;
import com.micro.salaryservice.service.impl.SalaryIncrementServiceImpl;
import com.micro.salaryservice.validator.SalaryIncrementValidator;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.core.KafkaTemplate;

class SalaryIncrementServiceImplTest {

    @InjectMocks
    private SalaryIncrementServiceImpl salaryIncrementService;

    @Mock
    private SalaryIncrementRepository salaryIncrementRepository;

    @Mock
    private SalaryIncrementValidator salaryIncrementValidator;

    @Mock
    private EmployeeClient employeeClient;

    @Mock
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Mock
    private RedisTemplate<String, SalaryIncrement> redisTemplate;

    @Mock
    private SalaryIncrementMapper salaryIncrementMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


}
