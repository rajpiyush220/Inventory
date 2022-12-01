package com.touchblankspot.inventory.portal.web.controller;

import static com.touchblankspot.inventory.portal.user.constant.RoleEnum.ADMIN;
import static com.touchblankspot.inventory.portal.user.constant.RoleEnum.SUPER_ADMIN;

import com.touchblankspot.inventory.portal.security.annotations.CanCreateUser;
import com.touchblankspot.inventory.portal.security.service.SecurityService;
import com.touchblankspot.inventory.portal.service.UserService;
import com.touchblankspot.inventory.portal.user.constant.RoleEnum;
import com.touchblankspot.inventory.portal.web.types.mapper.UserMapper;
import com.touchblankspot.inventory.portal.web.types.user.RegisterUserRequest;
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
public class RegisterUserController {

  @NonNull
  private final UserService userService;
  @NonNull
  private final SecurityService securityService;
  @NonNull
  private final UserMapper userMapper;

  @GetMapping("/user/register")
  @CanCreateUser
  public String registration(Model model) {
    model.addAttribute("selectedRole", RoleEnum.getRoleSelectTypes().get(0).name());
    model.addAttribute("roleSelectTypes", RoleEnum.getRoleSelectTypes());
    model.addAttribute("registrationForm", new RegisterUserRequest());
    return "auth/register/registration";
  }

  @PostMapping("/user/register")
  @CanCreateUser
  public String registration(
      @Valid @ModelAttribute("registrationForm") RegisterUserRequest userRequest,
      BindingResult bindingResult, Model model) {
    if (bindingResult.hasErrors()) {
      return "auth/register/registration";
    }
    try {
      String roleName =
          securityService.getCurrentUserRoles().contains(SUPER_ADMIN.name())
              ? ADMIN.name()
              : userRequest.getRoleName();
      userService.save(userMapper.toEntity(userRequest), roleName);
      model.addAttribute("successMessage", "User Created successfully.");
    } catch (Exception ex) {
      log.error("Unable to create user ", ex);
      model.addAttribute("errorMessage", "Unable to create user please contact administrator");
    }
    return "auth/register/registration";
  }
}
