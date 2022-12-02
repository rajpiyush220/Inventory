package com.touchblankspot.inventory.portal.web.controller;

import static com.touchblankspot.inventory.portal.user.constant.RoleEnum.SUPER_ADMIN;

import com.touchblankspot.inventory.portal.security.service.SecurityService;
import com.touchblankspot.inventory.portal.service.UserService;
import com.touchblankspot.inventory.portal.web.types.mapper.UserMapper;
import com.touchblankspot.inventory.portal.web.types.user.RegisterAdminRequest;
import jakarta.validation.Valid;
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
public class LoginController {

  @NonNull private final UserService userService;
  @NonNull private final SecurityService securityService;
  @NonNull private final UserMapper userMapper;

  @GetMapping("/register/super/admin")
  public String registration(Model model) {
    model.addAttribute("registrationForm", new RegisterAdminRequest());
    return "auth/login/registration";
  }

  @PostMapping("/register/super/admin")
  public String registration(
      @Valid @ModelAttribute("registrationForm") RegisterAdminRequest userRequest,
      BindingResult bindingResult,
      Model model) {
    if (bindingResult.hasErrors()) {
      return "auth/login/registration";
    }
    try {
      userService.save(userMapper.toEntity(userRequest), SUPER_ADMIN.name());
      model.addAttribute("successMessage", "User Created successfully. You can login now.");
    } catch (Exception ex) {
      log.error("Unable to create super admin ", ex);
      model.addAttribute("errorMessage", "Unable to create user please contact administrator");
    }
    return "auth/login/registration";
  }

  @GetMapping("/login")
  public String login(Model model, String error, String logout, String message) {
    if (securityService.isAuthenticated()) {
      return "redirect:/";
    }
    if (error != null) {
      model.addAttribute("error", "Your username and password is invalid.");
    }

    if (logout != null || message != null) {
      model.addAttribute("message", "You have been logged out successfully.");
    }
    model.addAttribute("isSuperAdminExists", !userService.isSuperAdminExists());
    return "auth/login/login";
  }

  @GetMapping({"/", "/welcome"})
  public String welcome() {
    if (!securityService.isAuthenticated()) {
      return "redirect:/login";
    }
    return "welcome";
  }
}
