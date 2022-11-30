package com.touchblankspot.inventory.types;

import com.touchblankspot.common.validator.FieldMatch;
import com.touchblankspot.common.validator.ValidPassword;
import lombok.Data;

@Data
@FieldMatch(
    first = "newPassword",
    second = "newPasswordConfirm",
    message = "Password and confirm password doesn't match")
public class ChangePasswordRequest {

  private String oldPassword;

  private String token;

  @ValidPassword private String newPassword;

  @ValidPassword private String newPasswordConfirm;
}
