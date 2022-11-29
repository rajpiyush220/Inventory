package com.touchblankspot.inventory.data.model;

import com.touchblankspot.common.data.model.embedded.Immutable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "role")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Role extends Immutable {

  @Column(name = "name", updatable = false, nullable = false, length = 20, unique = true)
  private String name;

  @ManyToMany(mappedBy = "roles")
  private Set<User> users;
}
