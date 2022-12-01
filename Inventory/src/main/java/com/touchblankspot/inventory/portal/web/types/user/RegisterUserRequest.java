package com.touchblankspot.inventory.portal.web.types.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RegisterUserRequest extends RegisterAdminRequest {
  private String roleName;
}
