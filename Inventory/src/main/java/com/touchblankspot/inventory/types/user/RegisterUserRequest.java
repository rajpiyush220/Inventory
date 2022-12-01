package com.touchblankspot.inventory.types.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterUserRequest extends RegisterAdminRequest{
  private String roleName;
}
