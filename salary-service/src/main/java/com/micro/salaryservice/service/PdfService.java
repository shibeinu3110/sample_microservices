package com.micro.salaryservice.service;

import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public interface PdfService {
    void exportPdfFile(HttpServletResponse response) throws IOException;
}
