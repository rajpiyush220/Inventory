package com.touchblankspot.inventory.controller;

import com.touchblankspot.inventory.pdf.Html2PdfService;
import java.io.IOException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Slf4j
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
public class PdfDownloadController {

  @NonNull private final Html2PdfService html2PdfService;

  @GetMapping("/download")
  public void download() throws IOException {
    html2PdfService.generatePdf("","");
  }
}
