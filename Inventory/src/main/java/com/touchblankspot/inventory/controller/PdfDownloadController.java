package com.touchblankspot.inventory.controller;

import com.touchblankspot.inventory.data.model.User;
import com.touchblankspot.inventory.mail.service.EmailService;
import com.touchblankspot.inventory.mail.template.service.ThymeleafTemplateService;
import com.touchblankspot.inventory.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;
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

  @NonNull
  private final EmailService emailService;

  @NonNull
  private final ThymeleafTemplateService thymeleafTemplateService;

  @NonNull
  private final UserService userService;

  @GetMapping("/download")
  public String download(HttpServletRequest request) throws IOException {

    User user = userService.findByUserName("rajpiyush220@gmail.com");
    thymeleafTemplateService.sendEmail(Map.of(
        "firstName", user.getFullName(),
        "userName", user.getUserName(),
        "email", user.getUserName(),
        "subject","Test thymleaf email"
    ), "email/thymeleaf/welcome");
    log.error("Email sent");
    return "login/login";
  }
}
