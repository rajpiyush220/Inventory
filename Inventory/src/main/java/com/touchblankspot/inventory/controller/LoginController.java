package com.touchblankspot.inventory.controller;

import com.touchblankspot.inventory.constant.RoleEnum;
import com.touchblankspot.inventory.service.SecurityService;
import com.touchblankspot.inventory.service.UserService;
import com.touchblankspot.inventory.types.mapper.UserMapper;
import com.touchblankspot.inventory.types.user.RegisterAdminRequest;
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

  @NonNull
  private final UserService userService;
  @NonNull
  private final SecurityService securityService;
  @NonNull
  private final UserMapper userMapper;

  @GetMapping("/register/super/admin")
  public String registration(Model model) {
    model.addAttribute("registrationForm", new RegisterAdminRequest());
    return "login/registration";
  }

  @PostMapping("/register/super/admin")
  public String registration(
      @Valid @ModelAttribute("registrationForm") RegisterAdminRequest userRequest,
      BindingResult bindingResult) {
    if (bindingResult.hasErrors()) {
      return "login/registration";
    }
    userService.save(userMapper.toEntity(userRequest), RoleEnum.SUPER_ADMIN.name());
    securityService.autoLogin(userRequest.getUserName(), userRequest.getPasswordConfirm());
    return "redirect:/welcome";
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
    return "login/login";
  }

  @GetMapping({"/", "/welcome"})
  public String welcome() {
    if (!securityService.isAuthenticated()) {
      return "redirect:/login";
    }
    return "welcome";
  }
}
