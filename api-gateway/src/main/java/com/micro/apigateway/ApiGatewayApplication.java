package com.micro.apigateway;

import com.micro.apigateway.config.SecretKeyProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication(scanBasePackages = {
		"com.micro.apigateway",
		"com.micro.commonlib.common.exception",  // Thêm package chứa GlobalExceptionHandler
})
@EnableDiscoveryClient
@EnableConfigurationProperties(SecretKeyProperties.class)
public class ApiGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiGatewayApplication.class, args);
	}

}
