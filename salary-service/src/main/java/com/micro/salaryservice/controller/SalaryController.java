package com.micro.salaryservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/salaries")
@RequiredArgsConstructor
public class SalaryController {
    @GetMapping()
    public String getSalaries() {
        return "Salaries data";
    }
}
