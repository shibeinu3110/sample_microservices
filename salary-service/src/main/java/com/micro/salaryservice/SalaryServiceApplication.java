package com.micro.salaryservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@SpringBootApplication(scanBasePackages = {
        "com.micro.salaryservice",
        "com.micro.commonlib.common.exception",  // Thêm package chứa GlobalExceptionHandler
})
@EnableDiscoveryClient
@EnableMongoAuditing
@EnableCaching
public class SalaryServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SalaryServiceApplication.class, args);
    }

}
