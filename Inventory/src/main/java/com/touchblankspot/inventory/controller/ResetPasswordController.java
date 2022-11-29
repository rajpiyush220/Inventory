package com.touchblankspot.inventory.controller;

import static com.touchblankspot.inventory.web.util.WebUtil.getPasswordResetUrl;

import com.touchblankspot.inventory.data.model.User;
import com.touchblankspot.inventory.mail.service.EmailSenderService;
import com.touchblankspot.inventory.service.SecurityService;
import com.touchblankspot.inventory.service.UserService;
import com.touchblankspot.inventory.types.ChangePasswordRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.util.Locale;
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
  private final EmailSenderService emailSenderService;

  @GetMapping("/resetPassword")
  public String resetPassword(HttpServletRequest request, String email) {
    if (securityService.isAuthenticated()) {
      return "redirect:/";
    }
    if (email != null) {
      User user = userService.findByUserName(email);
      if (user == null) {
        return "redirect:/login?error=" + "No user found with this email";
      }
      UUID token = UUID.randomUUID();
      userService.createPasswordResetTokenForUser(user, token);
      String url = getPasswordResetUrl(request, token);
      log.error(url);
      return "redirect:/login?error=" + "Unable to send reset link email now.";
      /*try {
        emailSenderService.sendPasswordResetEmail(
            getPasswordResetUrl(request, token), user.getUserName());
        return "redirect:/login?error=" + "You should receive an Password Reset Email shortly";
      } catch (Exception ex) {
        return "redirect:/login?error=" + "Unable to send reset link email now.";
      }*/
    }
    return "reset/forgotPassword";
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
      final Locale locale,
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
