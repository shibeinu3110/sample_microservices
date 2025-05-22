package com.micro.salaryservice.service.impl;


import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.*;
import com.micro.salaryservice.consts.ConstParameter;
import com.micro.salaryservice.model.SalaryIncrement;
import com.micro.salaryservice.repository.SalaryIncrementRepository;
import com.micro.salaryservice.service.PdfService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.IOException;
import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j(topic = "PDF-SERVICE-IMPL")
public class PdfServiceImpl implements PdfService {
    private final SalaryIncrementRepository salaryIncrementRepository;
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
