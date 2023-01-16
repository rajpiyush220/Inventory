package com.touchblankspot.inventory.portal.mail.template.service;

import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
public class FreeMarkerTemplateService {

  @Value("${application.email.sender}")
  private String sender;

  @NonNull private final Configuration configuration;

  public String getEmailContent(String templateName, Map<String, Object> dataMap)
      throws IOException, TemplateException {
    StringWriter stringWriter = new StringWriter();
    configuration.getTemplate(templateName).process(dataMap, stringWriter);
    return stringWriter.getBuffer().toString();
  }

  public MimeMessagePreparator constructEmailContent(
      Map<String, Object> dataMap, String templateName) {
    return mimeMessage -> {
      MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
      message.setTo(dataMap.get("email").toString());
      message.setFrom(sender);
      message.setSubject(dataMap.get("subject").toString());
      message.setText(getEmailContent(templateName, dataMap), true);
    };
  }

  public MimeMessagePreparator constructEmailContentWithAttachment(
      Map<String, Object> dataMap, String templateName) {
    String fileToAttach = dataMap.get("file_path").toString();
    String fileName = dataMap.get("file_name").toString();
    return mimeMessage -> {
      MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, "UTF-8");
      message.setTo(dataMap.get("email").toString());
      message.setFrom(sender);
      message.setSubject(dataMap.get("subject").toString());
      message.setText(getEmailContent(templateName, dataMap), true);
      FileSystemResource file = new FileSystemResource(new File(fileToAttach));
      message.addAttachment(fileName, file);
    };
  }
}
