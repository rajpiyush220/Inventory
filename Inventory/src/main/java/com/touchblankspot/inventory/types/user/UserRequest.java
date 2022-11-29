package com.touchblankspot.inventory.types.user;

import com.touchblankspot.common.validator.FieldMatch;
import com.touchblankspot.common.validator.UniqueField;
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
public class UserRequest {

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

  @Size(min = 8, max = 32, message = "Password must be between 8 and 32 character.")
  private String password;

  @Size(min = 8, max = 32, message = "PasswordConfirm must be between 8 and 32 character.")
  private String passwordConfirm;
}
