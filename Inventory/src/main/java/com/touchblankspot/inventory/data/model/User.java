package com.touchblankspot.inventory.data.model;

import com.touchblankspot.common.data.attribute.encryptor.AttributeEncryptor;
import com.touchblankspot.common.data.model.embedded.Mutable;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Transient;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@Entity(name = "user")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User extends Mutable {

  @Column(name = "first_name", nullable = false)
  private String firstName;

  @Column(name = "last_name")
  private String lastName;

  @Column(name = "username", unique = true, nullable = false)
  @Convert(converter = AttributeEncryptor.class)
  private String userName;

  @Column(name = "password")
  private String password;

  @ManyToMany(fetch = FetchType.EAGER)
  private Set<Role> roles;

  @Transient private Set<GrantedAuthority> authorities;

  public Set<GrantedAuthority> getAuthorities() {
    if (authorities == null || authorities.isEmpty()) {
      authorities =
          this.getRoles().stream()
              .map(Role::getName)
              .map(SimpleGrantedAuthority::new)
              .collect(Collectors.toSet());
    }
    return authorities;
  }
}
