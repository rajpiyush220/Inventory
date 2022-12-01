package com.touchblankspot.inventory.portal.mail.service;

import com.touchblankspot.inventory.portal.mail.template.service.FreeMarkerTemplateService;
import java.util.Map;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
public class EmailService {

  @Value("${application.email.sender:touchblankspot@gmail.com}")
  private String sender;

  @NonNull private final JavaMailSender mailSender;

  @NonNull private final FreeMarkerTemplateService freeMarkerTemplateService;

  public void sendPasswordResetEmail(Map<String, Object> dataMap) {
    mailSender.send(
        freeMarkerTemplateService.constructEmailContent(
            dataMap, "email/resetPasswordLinkEmail.ftlh"));
  }
}
