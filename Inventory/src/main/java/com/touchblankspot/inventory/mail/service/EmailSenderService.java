package com.touchblankspot.inventory.mail.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
public class EmailSenderService {

  @NonNull private final JavaMailSender mailSender;

  @NonNull private final EmailService emailService;

  public void sendPasswordResetEmail(String url, String recipient) {
    SimpleMailMessage mailMessage = emailService.constructResetTokenEmail(url, recipient);
    mailSender.send(mailMessage);
  }
}
