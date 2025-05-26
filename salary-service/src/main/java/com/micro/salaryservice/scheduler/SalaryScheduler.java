package com.micro.salaryservice.scheduler;

import com.micro.salaryservice.service.SalaryIncrementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j(topic = "SCHEDULER-SALARY-SERVICE")
public class SalaryScheduler {
    private final SalaryIncrementService service;
    @Scheduled(cron = "0 0 0 * * ?") // Every day at midnight
    public void clearExpiredSalaryIncrements() {
        log.info("Starting scheduled task to clear expired salary increments");
        service.scheduleSalaryIncrementEviction();
        log.info("Finished scheduled task to clear expired salary increments");
    }
}
