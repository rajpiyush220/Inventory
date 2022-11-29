package com.touchblankspot.inventory.types;

import com.touchblankspot.common.validator.FieldMatch;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@FieldMatch(
    first = "newPassword",
    second = "newPasswordConfirm",
    message = "Password and confirm password doesn't match")
public class ChangePasswordRequest {

  private String oldPassword;

  private String token;

  @Size(min = 8, max = 32, message = "Password must be between 8 and 32 character.")
  private String newPassword;

  @Size(min = 8, max = 32, message = "Password Confirm must be between 8 and 32 character.")
  private String newPasswordConfirm;
}
