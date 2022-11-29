package com.touchblankspot.inventory.mail.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
public class EmailService {

  @Value("${application.email.sender:touchblankspot@gmail.com}")
  private String sender;

  public SimpleMailMessage constructResetTokenEmail(String url, String recipient) {
    String message = "Reset Password";
    return constructEmail("Reset Password", message + " \r\n" + url, recipient);
  }

  private SimpleMailMessage constructEmail(String subject, String body, String recipient) {
    SimpleMailMessage email = new SimpleMailMessage();
    email.setSubject(subject);
    email.setText(body);
    email.setTo(recipient);
    email.setFrom(sender);
    return email;
  }
}
