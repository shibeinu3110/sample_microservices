package com.micro.departmentservice.config;

import com.micro.departmentservice.client.EmployeeClient;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.client.loadbalancer.reactive.LoadBalancedExchangeFilterFunction;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
@RequiredArgsConstructor
public class WebClientConfig {

    //define a load balanced exchange filter function
    //it can look up for service name instead of IP address
    private final LoadBalancedExchangeFilterFunction filterFunction;


    //we need to inject the load balanced exchange filter function
    //web client will make a call to the employee service
    //web client send a request to the employee service
    //create a web client bean to call the employee service
    @Bean
    public WebClient employeeWebClient() {
        return WebClient.builder()
                .baseUrl("http://employee-service") // Base URL for the employee service
                .filter(filterFunction)
                .build();
    }

    //create a proxy implementation for employee client so that EmployeeClient can call API in the employee service
    //we can use this client to call the employee service
    //employee client will be treated as a bean
    @Bean
    public EmployeeClient employeeClient() {
        HttpServiceProxyFactory factory = HttpServiceProxyFactory
                .builderFor(WebClientAdapter.create(employeeWebClient()))
                .build();
        return factory.createClient(EmployeeClient.class);
    }
}
