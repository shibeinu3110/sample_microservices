package com.micro.employeeservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.micro.employeeservice.dto.DepartmentDTO;
import com.micro.employeeservice.model.Employee;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "KAFKA-CONSUMER")
public class TestKafkaService {
    private final ObjectMapper objectMapper;
    @KafkaListener(topics = "test-topic", groupId = "group-id")
    public void consume(ConsumerRecord<String, Object> record) {
        Object value = record.value();
        log.info("Consumed raw message: {}", value);
        if(value instanceof Map<?,?>) {
            Map<String, Object> map = (Map<String, Object>) value;
            for(Map.Entry<String, Object> entry : map.entrySet()) {
                log.info("Key: {}, Value: {}", entry.getKey(), entry.getValue());
            }
            try {
                DepartmentDTO departmentDTO = objectMapper.convertValue(map, DepartmentDTO.class);
                log.info("Consumed message after converted: {}", departmentDTO);
                log.info("Department id {}", departmentDTO.getId());
                log.info("Department name {}", departmentDTO.getName());
                log.info("Department employees {}", departmentDTO.getEmployees());
            } catch (IllegalArgumentException e) {
                log.error("Failed to convert message to Employee: {}", e.getMessage());
            }
        } else {
            log.warn("Received message of unknown type: {}", record.getClass());
        }
    }
}
