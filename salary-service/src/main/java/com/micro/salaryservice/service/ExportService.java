package com.micro.salaryservice.service;

import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public interface ExportService {
    Object exportExcelFile(HttpServletResponse response);

    void exportPdfFile(HttpServletResponse response) throws IOException;
}
