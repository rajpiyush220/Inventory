package com.touchblankspot.inventory.portal.web.controller;

import com.touchblankspot.inventory.portal.data.model.User;
import com.touchblankspot.inventory.portal.mail.service.EmailService;
import com.touchblankspot.inventory.portal.mail.template.service.ThymeleafTemplateService;
import com.touchblankspot.inventory.portal.service.UserService;
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

  @NonNull private final EmailService emailService;

  @NonNull private final ThymeleafTemplateService thymeleafTemplateService;

  @NonNull private final UserService userService;

  @GetMapping("/download")
  public String download(HttpServletRequest request) {
   try {
     User user = userService.findByUserName("rajpiyush220@gmail.com");
     thymeleafTemplateService.sendEmail(
             Map.of(
                     "firstName", user.getFullName(),
                     "userName", user.getUserName(),
                     "email", user.getUserName(),
                     "subject", "Test thymleaf email"),
             "email/thymeleaf/welcome");
     log.info("Email sent");
   } catch (Exception ex) {
     log.error("Email doesn't send {}" , ex.getMessage());
   }
   return "login/login";
  }
}
