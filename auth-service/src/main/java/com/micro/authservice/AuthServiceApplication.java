package com.micro.authservice;

import com.micro.authservice.config.SecretKeyProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication(scanBasePackages = {
        "com.micro.authservice",
        "com.micro.commonlib.common.exception",  // Thêm package chứa GlobalExceptionHandler
})
@EnableDiscoveryClient
@EnableConfigurationProperties(SecretKeyProperties.class)
public class AuthServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthServiceApplication.class, args);
    }

}
