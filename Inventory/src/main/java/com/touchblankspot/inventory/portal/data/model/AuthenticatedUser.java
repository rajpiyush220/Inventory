package com.touchblankspot.inventory.portal.data.model;

import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

@Getter
@Setter
@ToString
public class AuthenticatedUser extends User {

  private String firstName;

  private String lastName;

  private String fullName;

  private String password;

  private String username;

  private Set<GrantedAuthority> authorities;

  private boolean accountNonExpired = true;

  private boolean accountNonLocked = true;

  private boolean credentialsNonExpired = true;

  private boolean enabled = true;

  public AuthenticatedUser(com.touchblankspot.inventory.portal.data.model.User user) {
    super(user.getUserName(), user.getPassword(), user.getAuthorities());
    firstName = user.getFirstName();
    lastName = user.getLastName();
    fullName = user.getFullName();
    username = user.getUserName();
    password = user.getPassword();
    authorities = user.getAuthorities();
  }
}
