package com.touchblankspot.inventory.mail.template.service;

import com.touchblankspot.inventory.data.model.User;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
public class ThymeleafTemplateService {

  @NonNull
  private final TemplateEngine templateEngine;

  @NonNull
  private final JavaMailSender mailSender;


  public String sendMail(User user) throws MessagingException {
    Context context = new Context();
    context.setVariable("user", user);

    String process = templateEngine.process("email/thymeleaf/welcome", context);
    MimeMessage mimeMessage = mailSender.createMimeMessage();
    MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
    helper.setSubject("Welcome " + user.getFullName());
    helper.setText(process, true);
    helper.setTo(user.getUserName());
    mailSender.send(mimeMessage);
    return "Sent";
  }

}
