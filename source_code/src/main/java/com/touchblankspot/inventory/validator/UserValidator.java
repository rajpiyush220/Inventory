package com.touchblankspot.inventory.validator;

import com.touchblankspot.inventory.service.UserService;
import com.touchblankspot.inventory.types.user.UserRequest;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@Slf4j
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
public class UserValidator implements Validator {
  @NonNull private final UserService userService;

  @Override
  public boolean supports(Class<?> aClass) {
    return UserRequest.class.equals(aClass);
  }

  @Override
  public void validate(Object o, Errors errors) {
    UserRequest userRequest = (UserRequest) o;

    /*ValidationUtils.rejectIfEmptyOrWhitespace(errors, "userName", "NotEmpty", "UserName can not be empty.");
    if (EmailValidator.isValidEmail(userRequest.getUserName())) {
        errors.rejectValue("userName", "Invalid.userForm.username","Please use valid email address.");
    }
    if (userRequest.getUserName().length() < 6 || userRequest.getUserName().length() > 32) {
        errors.rejectValue("userName", "Size.userForm.username","Please use between 6 and 32 characters.");
    }*/
    /*if (userService.findByUserName(userRequest.getUserName()) != null) {
        errors.rejectValue("userName", "Duplicate.userForm.username", "Someone already has that username.");
    }*/

    /*ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "NotEmpty", "Password can not be empty.");
    if (userRequest.getPassword().length() < 8 || userRequest.getPassword().length() > 32) {
        errors.rejectValue("password", "Size.userForm.password","Try one with a password between 8 and 32 characters.");
    }*/

    /* if (!userRequest.getPasswordConfirm().equals(userRequest.getPassword())) {
        errors.rejectValue("passwordConfirm", "Diff.userForm.passwordConfirm", "Password and confirm password doesn't match");
    }*/
  }
}
