package com.touchblankspot.inventory.portal.data.model;

import com.touchblankspot.common.data.model.embedded.Mutable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class RolePermission extends Mutable {

  @NonNull @NotNull @Column private String roleName;

  @Column private String permission;
}
