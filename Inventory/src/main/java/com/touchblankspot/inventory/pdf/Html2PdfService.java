package com.touchblankspot.inventory.pdf;

import com.itextpdf.html2pdf.HtmlConverter;
import java.io.File;
import java.io.IOException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
public class Html2PdfService {

  @Value("classpath:templates/pdf/pdf-input.html")
  private Resource pdfInputFile;

  public void generatePdf(String templateName, String outputName) throws IOException {
    HtmlConverter.convertToPdf(pdfInputFile.getFile(), new File("demo-html.pdf"));
  }
}
