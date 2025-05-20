package com.micro.emailservice.config;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@Validated
@ConfigurationProperties(prefix = "spring.sendgrid")
public class MailProperties {
    @NotBlank(message = "API key must not be blank")
    private String apiKey;
    @NotBlank(message = "Sender email must not be blank")
    private String fromEmail;
    @NotBlank(message = "Template ID must not be blank")
    private String templateId;

}
