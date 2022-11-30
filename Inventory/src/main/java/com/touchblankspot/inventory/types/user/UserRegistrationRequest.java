package com.touchblankspot.inventory.types.user;

import com.touchblankspot.common.validator.FieldMatch;
import com.touchblankspot.common.validator.UniqueField;
import com.touchblankspot.common.validator.ValidPassword;
import com.touchblankspot.inventory.service.UserService;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldMatch(
    first = "password",
    second = "passwordConfirm",
    message = "Password and confirm password doesn't match")
public class UserRegistrationRequest {

  @Size(min = 2, max = 30, message = "FirstName must be between 2 and 30 character.")
  private String firstName;

  @Size(max = 30, message = "LastName must be less than 30 character.")
  private String lastName;

  @Email(message = "Username should be a valid email address.")
  @Size(min = 6, max = 32, message = "Username must be between 6 and 32 character.")
  @UniqueField(
      service = UserService.class,
      fieldName = "userName",
      message = "Selected Username already taken.")
  private String userName;

  @ValidPassword
  private String password;

  @ValidPassword
  private String passwordConfirm;
}
