package com.touchblankspot.inventory.portal.service;

import com.touchblankspot.inventory.portal.mail.service.EmailService;
import com.touchblankspot.inventory.portal.util.ExcelGenerator;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
public class ReportService {

  @NonNull private final SalesDetailsService salesDetailsService;

  @NonNull private final EmailService emailService;

  @NonNull private final ExcelGenerator excelGenerator;

  @Value("${application.file.tempLocation}")
  private String basePath;

  @Value("${application.email.admin}")
  private String adminEmail;

  private static final List<String> REPORT_HEADERS =
      List.of(
          "Name",
          "Category",
          "Sub Category",
          "Quantity",
          "Unit Price",
          "Total Amount",
          "Discount Amount",
          "Pay Mode",
          "Transaction ID",
          "Operator",
          "Sold At");

  public String generateDailySalesDetailsReport(String reportDate) {
    String fileName = generateFileName(reportDate);
    String filePath = String.format("%s\\%s", basePath, fileName);
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
      excelGenerator.generateExcel(
          REPORT_HEADERS,
          salesDetailsService.getSalesDetailsReportDateByDate(reportDate),
          fileName,
          outputStream);
      outputStream.close();
      emailService.sendDailySalesReportEmail(dataMap);
      Files.deleteIfExists(Path.of(filePath));
    } catch (Exception e) {
      log.error("Unable to generate sales daily report", e);
    }
    return filePath;
  }

  private String generateFileName(String reportDate) {
    return String.format("SalesDetails_%s.xlsx", reportDate);
  }
}
