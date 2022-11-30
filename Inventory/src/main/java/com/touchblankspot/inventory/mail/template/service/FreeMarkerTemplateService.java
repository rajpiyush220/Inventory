package com.touchblankspot.inventory.mail.template.service;

import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
public class FreeMarkerTemplateService {

  @NonNull private final Configuration configuration;

  @NonNull private final JavaMailSender mailSender;

  public String getEmailContent(String templateName, Map<String, Object> dataMap)
      throws IOException, TemplateException {
    StringWriter stringWriter = new StringWriter();
    configuration.getTemplate(templateName).process(dataMap, stringWriter);
    return stringWriter.getBuffer().toString();
  }

  public MimeMessage constructEmailContent(Map<String, Object> dataMap, String templateName)
      throws MessagingException, TemplateException, IOException {
    MimeMessage mimeMessage = mailSender.createMimeMessage();
    MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
    helper.setSubject(dataMap.get("subject").toString());
    helper.setTo(dataMap.get("email").toString());
    helper.setText(getEmailContent(templateName, dataMap), true);
    return mimeMessage;
  }
}
