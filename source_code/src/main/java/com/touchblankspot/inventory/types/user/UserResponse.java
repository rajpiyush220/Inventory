package com.touchblankspot.inventory.types.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {

  private String username;

  private String password;

  private String passwordConfirm;
}
