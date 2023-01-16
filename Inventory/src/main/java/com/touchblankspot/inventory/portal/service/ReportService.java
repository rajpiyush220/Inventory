package com.touchblankspot.inventory.portal.service;

import com.touchblankspot.inventory.portal.mail.service.EmailService;
import com.touchblankspot.inventory.portal.web.types.sales.SalesDetailResponseType;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
public class ReportService {

  @NonNull private final SalesDetailsService salesDetailsService;

  @NonNull private final EmailService emailService;

  private XSSFWorkbook workbook;
  private XSSFSheet sheet;

  @Value("${application.file.tempLocation}")
  private String basePath;

  @Value("${application.email.admin}")
  private String adminEmail;

  public String generateDailySalesDetailsReport(String reportDate) {
    String fileName = generateFileName(reportDate);
    String filePath = String.format("%s\\%s", basePath, fileName);
    List<SalesDetailResponseType> salesDetailResponseTypes =
        salesDetailsService.getSalesDetailsByDate(reportDate);
    workbook = new XSSFWorkbook();
    writeHeader();
    write(salesDetailResponseTypes);
    Map<String, Object> dataMap =
        Map.of(
            "report_date",
            reportDate,
            "file_path",
            filePath,
            "file_name",
            fileName,
            "email",
            adminEmail,
            "subject",
            "Daily sales Detail report for " + reportDate);
    try {
      OutputStream outputStream = new FileOutputStream(filePath);
      workbook.write(outputStream);
      workbook.close();
      outputStream.close();
      emailService.sendDailySalesReportEmail(dataMap);
      Files.deleteIfExists(Path.of(filePath));
    } catch (Exception e) {
      log.error("Unable to generate sales daily report", e);
    }
    return filePath;
  }

  private void writeHeader() {
    sheet = workbook.createSheet(generateSheetName(""));
    Row row = sheet.createRow(0);
    CellStyle style = workbook.createCellStyle();
    XSSFFont font = workbook.createFont();
    font.setBold(true);
    font.setFontHeight(16);
    style.setFont(font);
    createCell(row, 0, "Name", style);
    createCell(row, 1, "Category", style);
    createCell(row, 2, "Sub Category", style);
    createCell(row, 3, "Quantity", style);
    createCell(row, 4, "Unit Price", style);
    createCell(row, 5, "Total Amount", style);
    createCell(row, 6, "Discount Amount", style);
    createCell(row, 7, "Pay Mode", style);
    createCell(row, 8, "Transaction ID", style);
    createCell(row, 9, "Operator", style);
    createCell(row, 10, "Sold At", style);
  }

  private void createCell(Row row, int columnCount, Object valueOfCell, CellStyle style) {
    sheet.autoSizeColumn(columnCount);
    Cell cell = row.createCell(columnCount);
    if (valueOfCell instanceof Integer) {
      cell.setCellValue((Integer) valueOfCell);
    } else if (valueOfCell instanceof Long) {
      cell.setCellValue((Long) valueOfCell);
    } else if (valueOfCell instanceof String) {
      cell.setCellValue((String) valueOfCell);
    } else {
      cell.setCellValue((Boolean) valueOfCell);
    }
    cell.setCellStyle(style);
  }

  private void write(List<SalesDetailResponseType> datas) {
    AtomicInteger rowCount = new AtomicInteger(1);
    CellStyle style = workbook.createCellStyle();
    XSSFFont font = workbook.createFont();
    font.setFontHeight(14);
    style.setFont(font);

    datas.forEach(
        data -> {
          Row row = sheet.createRow(rowCount.getAndIncrement());
          int columnCount = 0;
          createCell(row, columnCount++, data.getName(), style);
          createCell(row, columnCount++, data.getProductCategory(), style);
          createCell(row, columnCount++, data.getSubCategory(), style);
          createCell(row, columnCount++, data.getQuantity(), style);
          createCell(row, columnCount++, data.getUnitPrice(), style);
          createCell(row, columnCount++, data.getTotalPrice(), style);
          createCell(row, columnCount++, data.getDiscountAmount(), style);
          createCell(row, columnCount++, data.getPaymentMode(), style);
          createCell(row, columnCount++, data.getTransactionId(), style);
          createCell(row, columnCount++, data.getOperatorName(), style);
          createCell(row, columnCount++, data.getSoldAt(), style);
        });
  }

  private String generateSheetName(String reportDate) {
    return String.format("SalesDetails_%s", reportDate);
  }

  private String generateFileName(String reportDate) {
    return generateSheetName(reportDate) + ".xlsx";
  }
}
