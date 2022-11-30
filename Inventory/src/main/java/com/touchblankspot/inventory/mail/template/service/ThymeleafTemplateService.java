package com.touchblankspot.inventory.mail.template.service;

import com.touchblankspot.inventory.data.model.User;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.util.Locale;
import java.util.Map;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
public class ThymeleafTemplateService {

  @Value("${application.email.sender}")
  private String sender;

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

  public void sendEmail(
      Map<String, Object> dataMap, String templateName) {
    mailSender.send(prepareEmail(
        dataMap, templateName));
  }

  public MimeMessagePreparator prepareEmail(
      Map<String, Object> dataMap, String templateName) {
    return mimeMessage -> {
      MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
      message.setTo(dataMap.get("email").toString());
      message.setFrom(sender);
      message.setSubject(dataMap.get("subject").toString());
      message.setText(templateEngine.process(templateName,
          new Context(Locale.getDefault(), dataMap)), true);
    };
  }
}
