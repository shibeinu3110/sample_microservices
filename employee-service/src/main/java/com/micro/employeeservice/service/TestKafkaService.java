package com.micro.employeeservice.service;

import com.micro.employeeservice.model.Employee;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class TestKafkaService {

    @KafkaListener(topics = "test-topic", groupId = "group-id")
    public void consume(Object s) {
        log.info("Consumed message: {}", s);
        System.out.println("Consumed message: " + s);
    }
}
