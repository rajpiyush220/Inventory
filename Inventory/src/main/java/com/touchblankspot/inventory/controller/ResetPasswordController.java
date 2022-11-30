package com.touchblankspot.inventory.controller;

import static com.touchblankspot.inventory.web.util.WebUtil.getAppUrl;
import static com.touchblankspot.inventory.web.util.WebUtil.getPasswordResetUrl;

import com.touchblankspot.inventory.data.model.User;
import com.touchblankspot.inventory.mail.service.EmailService;
import com.touchblankspot.inventory.service.SecurityService;
import com.touchblankspot.inventory.service.UserService;
import com.touchblankspot.inventory.types.ChangePasswordRequest;
import com.touchblankspot.inventory.types.ResetPasswordRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@Slf4j
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
public class ResetPasswordController {

  @NonNull private final UserService userService;

  @NonNull private final SecurityService securityService;

  @NonNull private final EmailService emailService;

  @GetMapping("/resetPassword")
  public String resetPassword(HttpServletRequest request, Model model) {
    if (securityService.isAuthenticated()) {
      return "redirect:/";
    }
    model.addAttribute("resetPwdForm", new ResetPasswordRequest());
    return "reset/forgotPassword";
  }

  @PostMapping("/resetPassword")
  public String resetPassword(
      HttpServletRequest request,
      @Valid @ModelAttribute("resetPwdForm") ResetPasswordRequest passwordRequest,
      BindingResult bindingResult) {

    if (bindingResult.hasErrors()) {
      return "reset/forgotPassword";
    }

    User user = userService.findByUserName(passwordRequest.getEmail());
    if (user == null) {
      return "redirect:/login?error=" + "No user found with this email";
    }
    UUID token = UUID.randomUUID();
    userService.createPasswordResetTokenForUser(user, token);
    String url = getPasswordResetUrl(request, token);
    log.error(url);
    try {
      Map<String, Object> dataMap =
          Map.of(
              "base_url", getAppUrl(request),
              "action_url", getPasswordResetUrl(request, token),
              "name", user.getFullName(),
              "email", user.getUserName(),
              "subject", "99Mall Reset Password Link");
      emailService.sendPasswordResetEmail(dataMap);
      return "redirect:/login?error=" + "You should receive an Password Reset Email shortly";
    } catch (Exception ex) {
      return "redirect:/login?error=" + "Unable to send reset link email now.";
    }
  }

  @GetMapping("/changePassword")
  public String showChangePasswordPage(Model model, String token) {
    String result = userService.validatePasswordResetToken(token);
    if (result != null) {
      return "redirect:/login?message=" + result;
    } else {
      ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest();
      changePasswordRequest.setToken(token);
      model.addAttribute("changePasswordForm", changePasswordRequest);
      return "reset/updatePassword";
    }
  }

  @PostMapping("/savePassword")
  public String savePassword(
      @Valid @ModelAttribute("changePasswordForm") ChangePasswordRequest passwordRequest,
      BindingResult bindingResult) {
    if (bindingResult.hasErrors()) {
      return "reset/updatePassword";
    }
    String result = userService.validatePasswordResetToken(passwordRequest.getToken());
    if (result != null) {
      return result;
    }
    Optional<User> user = userService.getUserByPasswordResetToken(passwordRequest.getToken());
    if (user.isPresent()) {
      userService.changeUserPassword(user.get(), passwordRequest.getNewPassword());
      // TODO : Create successfull or fail page
      return "Password reset successfully";
    } else {
      return "Invalid token.";
    }
  }
}
