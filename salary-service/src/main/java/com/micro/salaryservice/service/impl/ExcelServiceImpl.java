package com.micro.salaryservice.service.impl;

import com.micro.commonlib.common.exception.ErrorMessages;
import com.micro.commonlib.common.exception.StandardException;
import com.micro.salaryservice.consts.ConstParameter;
import com.micro.salaryservice.model.SalaryIncrement;
import com.micro.salaryservice.repository.SalaryIncrementRepository;
import com.micro.salaryservice.service.ExcelService;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Slf4j(topic = "EXCEL-SERVICE-IMPL")
@Service
@RequiredArgsConstructor
public class ExcelServiceImpl implements ExcelService {
    private final SalaryIncrementRepository salaryIncrementRepository;
    @Override
    public Object exportExcelFile(HttpServletResponse response) {
        Workbook workbook = new XSSFWorkbook();
        createSheetAndHeader(workbook);
        response.setContentType(ConstParameter.CONTENT_TYPE);
        response.setHeader(ConstParameter.KEY, ConstParameter.VALUE);
        try {
            ServletOutputStream sos = response.getOutputStream();
            workbook.write(sos);
            workbook.close();
            sos.flush();
            sos.close();
            return HttpStatus.OK;

        } catch (IOException e) {
            throw new StandardException(ErrorMessages.BAD_REQUEST, "Error while writing excel file");
        }
    }

    private void createSheetAndHeader(Workbook workbook) {
        Sheet sheet = workbook.createSheet(ConstParameter.SHEET_NAME);
        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < ConstParameter.HEADER_TITLE.length ; i++) {
            headerRow.createCell(i).setCellValue(ConstParameter.HEADER_TITLE[i]);
            sheet.setColumnWidth(i, ConstParameter.COLUMN_WIDTH);
        }
        insertData(sheet);

    }

    private void insertData(Sheet sheet) {
        List<SalaryIncrement> salaryIncrements = salaryIncrementRepository.findAll();

        for (int i = 0; i < salaryIncrements.size(); i++) {
            SalaryIncrement salaryIncrement = salaryIncrements.get(i);
            Row row = sheet.createRow(i + 1);
            row.createCell(0).setCellValue(safeString(salaryIncrement.getSalaryIncrementId()));
            row.createCell(1).setCellValue(safeString(salaryIncrement.getEmployeeId()));
            row.createCell(2).setCellValue(safeString(salaryIncrement.getIncrementAmount()));
            row.createCell(3).setCellValue(safeString(salaryIncrement.getCreatedDate()));
            row.createCell(4).setCellValue(safeString(salaryIncrement.getStatus()));
            row.createCell(5).setCellValue(safeString(salaryIncrement.getCreatedBy()));
            row.createCell(6).setCellValue(safeString(salaryIncrement.getEndDate()));
            row.createCell(7).setCellValue(safeString(salaryIncrement.getEndBy()));
            row.createCell(8).setCellValue(safeString(salaryIncrement.getLeaderNote()));
        }
    }
    private String safeString(Object value) {
        return value != null ? value.toString() : "null";
    }
}
