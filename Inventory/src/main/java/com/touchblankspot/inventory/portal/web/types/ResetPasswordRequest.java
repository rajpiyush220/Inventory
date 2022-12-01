package com.touchblankspot.inventory.portal.web.types;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResetPasswordRequest {

  @Email(message = "Email should be a valid email address.")
  @Size(min = 6, max = 32, message = "Email must be between 6 and 32 character.")
  private String email;
}
