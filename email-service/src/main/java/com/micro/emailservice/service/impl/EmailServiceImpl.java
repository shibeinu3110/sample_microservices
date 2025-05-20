package com.micro.emailservice.service.impl;


import com.micro.emailservice.config.MailProperties;
import com.sendgrid.Method;
import com.sendgrid.SendGrid;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Email;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;

import org.springframework.stereotype.Service;
import com.sendgrid.helpers.mail.objects.Personalization;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailServiceImpl {
    private final SendGrid sendGrid;
    private final MailProperties mailProperties;

    private String from;
    private String templateId;
    @PostConstruct
    void init() {
        this.from = mailProperties.getFromEmail();
        this.templateId = mailProperties.getTemplateId();
    }

    @KafkaListener(topics = "email-topic", groupId = "salary-increment")
    public void sendEmail(ConsumerRecord<String, Object> record) {
        Object value = record.value();
        if(value instanceof Map<?,?>) {
            Map<String, Object> map = (Map<String, Object>) value;
            try {
                String salaryIncrementId = (String) map.get("salaryIncrementId");

                //handle amount
                Object incrementAmountObj = map.get("incrementAmount");
                Long incrementAmount = null;
                if (incrementAmountObj instanceof Integer) {
                    incrementAmount = ((Integer) incrementAmountObj).longValue();
                } else if (incrementAmountObj instanceof Long) {
                    incrementAmount = (Long) incrementAmountObj;
                } else {
                    log.error("Unexpected type for incrementAmount: {}", incrementAmountObj.getClass());
                }


                //handle created date
                Object createdDateObj = map.get("createdDate");
                LocalDate createdDate = null;
                if (createdDateObj instanceof String) {
                    createdDate = LocalDate.parse((String) createdDateObj);
                } else if (createdDateObj instanceof LocalDate) {
                    createdDate = (LocalDate) createdDateObj;
                } else if(createdDateObj instanceof List<?>) {
                    List<?> list = (List<?>) createdDateObj;
                    if (list.size() == 3) {
                        int year = (Integer) list.get(0);
                        int month = (Integer) list.get(1);
                        int day = (Integer) list.get(2);
                        createdDate = LocalDate.of(year, month, day);
                    } else {
                        log.error("Unexpected size for createdDate list: {}", list.size());
                    }
                } else {
                    log.error("Unexpected type for createdDate: {}", createdDateObj.getClass());
                }

                //handle employee name
                String employeeName = (String) map.get("employeeName");
                //handle employee email
                String employeeEmail = (String) map.get("employeeEmail");


                //sending email
                log.info("Sending email to: {}", employeeEmail);
                Email fromEmail = new Email(from, "Hung Shiba");
                Email toEmail = new Email(employeeEmail);
                String subject = "You are being considered for a salary increase";

                Map<String, String> data = new HashMap<>();
                data.put("id", salaryIncrementId);
                data.put("amount", incrementAmount.toString());
                data.put("create_at", createdDate.toString());
                data.put("name", employeeName);

                Mail mail = new Mail();
                mail.setFrom(fromEmail);
                mail.setSubject(subject);

                Personalization personalization = new Personalization();
                personalization.addTo(toEmail);
                data.forEach(personalization::addDynamicTemplateData);
                mail.addPersonalization(personalization);
                mail.setTemplateId(templateId);

                Request request = new Request();
                try {
                    request.setMethod(Method.POST);
                    request.setEndpoint("mail/send");
                    request.setBody(mail.build());

                    Response response = sendGrid.api(request);
                    if(response.getStatusCode() == 202) {
                        log.info("Mail sent successfully");
                    } else {
                        log.error("Failed to send email. Response: {}", response.getBody());
                    }
                } catch (IOException e) {
                    log.error("Error when sending email");
                }
            } catch (Exception e) {
                log.error("Error processing message: {}", e.getMessage());
            }

        } else {
            log.warn("Received message of unknown type: {}", record.getClass());
        }
    }
}
