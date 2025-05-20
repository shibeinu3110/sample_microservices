package com.micro.emailservice.config;



import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.sendgrid.SendGrid;
@Configuration
@RequiredArgsConstructor
public class AppConfig {
    private final MailProperties mailProperties;
    private String sendGridApiKey;
    @PostConstruct
    void init() {
        this.sendGridApiKey= mailProperties.getApiKey();
    }

    @Bean
    public SendGrid sendGrid() {
        return new SendGrid(sendGridApiKey);
    }
}
