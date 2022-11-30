package com.touchblankspot.inventory.data.model;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

@Getter
@Setter
public class AuthenticatedUser extends User {

  private String firstName;

  private String lastName;

  private String password;

  private String username;

  private Set<GrantedAuthority> authorities;

  private boolean accountNonExpired = true;

  private boolean accountNonLocked = true;

  private boolean credentialsNonExpired = true;

  private boolean enabled = true;

  public AuthenticatedUser(
      String username, String password, Collection<? extends GrantedAuthority> authorities) {
    super(username, password, authorities);
    this.username = username;
    this.password = password;
    this.authorities = authorities.stream().collect(Collectors.toSet());
  }

  public AuthenticatedUser(
      String username,
      String password,
      boolean enabled,
      boolean accountNonExpired,
      boolean credentialsNonExpired,
      boolean accountNonLocked,
      Collection<? extends GrantedAuthority> authorities) {
    super(
        username,
        password,
        enabled,
        accountNonExpired,
        credentialsNonExpired,
        accountNonLocked,
        authorities);
    this.username = username;
    this.password = password;
    this.authorities = authorities.stream().collect(Collectors.toSet());
  }
}
