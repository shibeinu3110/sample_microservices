package com.micro.salaryservice.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.micro.salaryservice.model.SalaryIncrement;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String, SalaryIncrement> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, SalaryIncrement> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        // still save key as String
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());


        //object mapper to handle date & time
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();


        // need to convert SalaryIncrement to JSON before saving
        // automatically convert SalaryIncrement to JSON
        Jackson2JsonRedisSerializer<SalaryIncrement> serializer = new Jackson2JsonRedisSerializer<>(objectMapper, SalaryIncrement.class);
        template.setValueSerializer(serializer);
        template.setHashValueSerializer(serializer);
        return template;
    }

}