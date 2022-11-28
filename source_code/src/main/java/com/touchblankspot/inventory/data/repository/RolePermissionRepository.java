package com.touchblankspot.inventory.data.repository;

import com.touchblankspot.inventory.data.model.RolePermission;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface RolePermissionRepository extends JpaRepository<RolePermission, UUID> {

  List<RolePermission> findByRoleName(String roleName);

  @Modifying
  @Transactional
  long deleteByRoleNameAndPermissionIn(String roleName, List<String> permission);
}
