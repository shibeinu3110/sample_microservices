package com.micro.salaryservice.service;


import java.io.OutputStream;

public interface ExportService {
    void export(OutputStream outputStream);
    String getType();
}
