package com.micro.apigateway.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration

public class JacksonConfig {

    //custom ObjectMapper to handle LocalDateTime java 8 for prettier print
    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        // Đăng ký module hỗ trợ Java 8 Date/Time API
        mapper.registerModule(new JavaTimeModule());
        // Tắt tính năng ghi LocalDateTime dưới dạng timestamp
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return mapper;
    }
}