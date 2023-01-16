package com.touchblankspot.inventory.portal.util;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ExcelGenerator {

  private XSSFWorkbook workbook;
  private XSSFSheet sheet;

  public void generateExcel(
      List<String> headers, List<List<String>> datas, String fileName, OutputStream outputStream)
      throws IOException {
    if (!headers.isEmpty() && !datas.isEmpty() && headers.size() != datas.get(0).size()) {
      throw new IllegalArgumentException("Column count for header and data should be same");
    }
    workbook = new XSSFWorkbook();
    writeHeader(headers, fileName);
    writeData(datas);
    workbook.write(outputStream);
    workbook.close();
  }

  private void writeHeader(List<String> headers, String sheetName) {
    sheet = workbook.createSheet(sheetName);
    Row row = sheet.createRow(0);
    CellStyle style = workbook.createCellStyle();
    XSSFFont font = workbook.createFont();
    font.setBold(true);
    font.setFontHeight(16);
    style.setFont(font);
    AtomicInteger columnCount = new AtomicInteger(0);
    headers.forEach(header -> createCell(row, columnCount.getAndIncrement(), header, style));
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

  private void writeData(List<List<String>> datas) {
    AtomicInteger rowCount = new AtomicInteger(1);
    CellStyle style = workbook.createCellStyle();
    XSSFFont font = workbook.createFont();
    font.setFontHeight(14);
    style.setFont(font);
    datas.forEach(
        data -> {
          Row row = sheet.createRow(rowCount.getAndIncrement());
          AtomicInteger columnCount = new AtomicInteger(0);
          data.forEach(content -> createCell(row, columnCount.getAndIncrement(), content, style));
        });
  }
}
