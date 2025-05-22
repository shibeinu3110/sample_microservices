package com.micro.salaryservice.consts;

public class ConstParameter {
    public static final String SHEET_NAME = "Salary Increment Information";
    public static final String CONTENT_TYPE = "application/octet-stream";
    public static final String KEY = "Content-Disposition";
    public static final String VALUE = "attachment; filename=salary-increment.xlsx";
    public static final String[] HEADER_TITLE = {"ID", "Employee ID", "Increment Amount", "Created Date", "Status", "Created by", "End Date", "End By", "Leader Note"};
    public static final int COLUMN_WIDTH = 4000;
}
