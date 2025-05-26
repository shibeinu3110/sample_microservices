package com.micro.salaryservice.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.micro.salaryservice.model.SalaryIncrement;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

@Configuration
public class RedisConfig {


    //config for redisTemplate (when you use like redisTemplate.opsForValue().set("key", value))
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

    //config for starter cache (when you use @Cacheable, @CachePut, @CacheEvict)
    @Bean
    public CacheManager cacheManager(RedisConnectionFactory connectionFactory) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();

        //config object mapper for mapping type
        //activate ability to save metadata about type into json
        //so that redis can deserialize it back to the correct type
        objectMapper.activateDefaultTyping(
                objectMapper.getPolymorphicTypeValidator(),
                ObjectMapper.DefaultTyping.NON_FINAL
        );


        Jackson2JsonRedisSerializer<Object> serializer = new Jackson2JsonRedisSerializer<>(objectMapper, Object.class);
        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(serializer))
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                .entryTtl(Duration.ofMinutes(30)); //set time to live
        return RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(redisCacheConfiguration)
                .build();

    }

}