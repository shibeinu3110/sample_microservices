package com.micro.salaryservice.service;

import jakarta.servlet.http.HttpServletResponse;

public interface ExcelService {
    Object exportExcelFile(HttpServletResponse response);
}
