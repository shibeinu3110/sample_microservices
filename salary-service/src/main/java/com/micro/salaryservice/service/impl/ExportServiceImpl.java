package com.micro.salaryservice.service.impl;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.micro.commonlib.common.exception.ErrorMessages;
import com.micro.commonlib.common.exception.StandardException;
import com.micro.salaryservice.consts.ConstParameter;
import com.micro.salaryservice.model.SalaryIncrement;
import com.micro.salaryservice.repository.SalaryIncrementRepository;
import com.micro.salaryservice.service.ExportService;
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

import java.awt.*;
import java.io.IOException;
import java.util.List;

@Slf4j(topic = "EXCEL-SERVICE-IMPL")
@Service
@RequiredArgsConstructor
public class ExportServiceImpl implements ExportService {
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


    @Override
    public void exportPdfFile(HttpServletResponse response) throws IOException {

        response.setContentType(ConstParameter.CONTENT_TYPE);
        response.setHeader(ConstParameter.KEY, ConstParameter.VALUE_PDF);


        Document document = new Document(PageSize.A4);
        // convert the document to pdf
        PdfWriter.getInstance(document, response.getOutputStream());

        document.open();

        Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        font.setSize(18);
        font.setColor(Color.BLUE);

        Paragraph paragraph = new Paragraph("Salary Increment List", font);
        paragraph.setAlignment(Paragraph.ALIGN_CENTER);

        document.add(paragraph);

        PdfPTable table = new PdfPTable(ConstParameter.HEADER_TITLE.length);
        log.info("Table created");
        table.setWidthPercentage(100f);
        table.setSpacingBefore(10);

        writePdfHeader(table);
        writePdfData(table);

        document.add(table);
        document.close();
    }

    private void writePdfHeader(PdfPTable table) {
        for(int i = 0; i < ConstParameter.HEADER_TITLE.length; i++) {
            PdfPCell cell = new PdfPCell();
            cell.setBackgroundColor(Color.CYAN);
            cell.setPadding(5);
            cell.setPhrase(new Phrase(ConstParameter.HEADER_TITLE[i]));
            table.addCell(cell);
        }
    }

    private void writePdfData(PdfPTable table) {
        List<SalaryIncrement> salaryIncrementList = salaryIncrementRepository.findAll();
        for(SalaryIncrement salaryIncrement : salaryIncrementList) {
            table.addCell(safeString(salaryIncrement.getSalaryIncrementId()));
            table.addCell(safeString(salaryIncrement.getEmployeeId()));
            table.addCell(safeString(salaryIncrement.getIncrementAmount()));
            table.addCell(safeString(salaryIncrement.getCreatedDate()));
            table.addCell(safeString(salaryIncrement.getStatus()));
            table.addCell(safeString(salaryIncrement.getCreatedBy()));
            table.addCell(safeString(salaryIncrement.getEndDate()));
            table.addCell(safeString(salaryIncrement.getEndBy()));
            table.addCell(safeString(salaryIncrement.getLeaderNote()));
        }
    }

    private String safeString(Object value) {
        return value != null ? value.toString() : "null";
    }









}
