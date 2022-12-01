package com.touchblankspot.inventory.portal.web.controller;

import static com.touchblankspot.inventory.portal.web.util.WebUtil.getAppUrl;
import static com.touchblankspot.inventory.portal.web.util.WebUtil.getPasswordResetUrl;

import com.touchblankspot.inventory.portal.data.model.User;
import com.touchblankspot.inventory.portal.mail.service.EmailService;
import com.touchblankspot.inventory.portal.security.service.SecurityService;
import com.touchblankspot.inventory.portal.service.UserService;
import com.touchblankspot.inventory.portal.web.types.ChangePasswordRequest;
import com.touchblankspot.inventory.portal.web.types.ResetPasswordRequest;
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

  @NonNull
  private final UserService userService;

  @NonNull
  private final SecurityService securityService;

  @NonNull
  private final EmailService emailService;

  private final String RESET_PWD_FAIL_FORMAT = "auth/reset/forgotPassword?error=%s";
  private final String RESET_PWD_SUCCESS_FORMAT = "auth/reset/forgotPassword?error=%s";


  @GetMapping("/resetPassword")
  public String resetPassword(HttpServletRequest request, Model model, String error,
                              String message) {
    if (securityService.isAuthenticated()) {
      return "redirect:/";
    }
    model.addAttribute("resetPwdForm", new ResetPasswordRequest());
    return "auth/reset/forgotPassword";
  }

  @PostMapping("/resetPassword")
  public String resetPassword(
      HttpServletRequest request,
      @Valid @ModelAttribute("resetPwdForm") ResetPasswordRequest passwordRequest,
      BindingResult bindingResult) {
    if (bindingResult.hasErrors()) {
      return "auth/reset/forgotPassword";
    }
    User user = userService.findByUserName(passwordRequest.getEmail());
    if (user == null) {
      return "auth/reset/forgotPassword?error=" + "No user found with this email";
    }
    UUID token = UUID.randomUUID();
    userService.createPasswordResetTokenForUser(user, token);
    String url = getPasswordResetUrl(request, token);
    log.error(url);
    System.out.println(url);
    try {
      Map<String, Object> dataMap =
          Map.of(
              "base_url", getAppUrl(request),
              "action_url", getPasswordResetUrl(request, token),
              "name", user.getFullName(),
              "email", user.getUserName(),
              "subject", "99Mall Reset Password Link");
      emailService.sendPasswordResetEmail(dataMap);

      return "auth/reset/forgotPassword?error=" +
          "You should receive an Password Reset Email shortly";
    } catch (Exception ex) {
      return "auth/reset/forgotPassword?error=" + "Unable to send reset link email now.";
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
      return "auth/reset/updatePassword";
    }
  }

  @PostMapping("/savePassword")
  public String savePassword(
      @Valid @ModelAttribute("changePasswordForm") ChangePasswordRequest passwordRequest,
      BindingResult bindingResult) {
    if (bindingResult.hasErrors()) {
      return "auth/reset/updatePassword";
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
