package com.touchblankspot.inventory.types.user;

import com.touchblankspot.common.validator.EqualFields;
import com.touchblankspot.common.validator.UniqueField;
import com.touchblankspot.inventory.service.UserService;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest {

  @NotEmpty(message = "UserName can not be empty.")
  @Email(message = "Username should be a valid email address.")
  @Size(min = 6, max = 32, message = "Username must be between 6 and 32 character.")
  @UniqueField(
      service = UserService.class,
      fieldName = "userName",
      message = "Selected Username already taken.")
  private String userName;

  @NotEmpty(message = "Password can not be empty.")
  @Size(min = 8, max = 32, message = "Password must be between 8 and 32 character.")
  private String password;

  @NotEmpty(message = "PasswordConfirm can not be empty.")
  @Size(min = 8, max = 32, message = "PasswordConfirm must be between 8 and 32 character.")
  @EqualFields(
      baseField = "password",
      matchField = "passwordConfirm",
      message = "Password and confirm password doesn't match")
  private String passwordConfirm;
}