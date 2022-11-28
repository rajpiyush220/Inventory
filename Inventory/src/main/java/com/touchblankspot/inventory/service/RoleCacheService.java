package com.touchblankspot.inventory.service;

import com.touchblankspot.inventory.data.model.RolePermission;
import com.touchblankspot.inventory.data.repository.RolePermissionRepository;
import java.util.List;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
public class RoleCacheService {

  @NonNull private final RolePermissionRepository rolePermissionRepository;

  @Cacheable(value = "RolePermissions", key = "#roleName")
  public List<String> getRolePermissions(String roleName) {
    return rolePermissionRepository.findByRoleName(roleName).stream()
        .map(RolePermission::getPermission)
        .toList();
  }

  @CacheEvict(value = "RolePermissions", key = "#roleName")
  public void clearRolePermissionCache(String roleName) {
    log.info("Clearing existing permission for role from memory");
  }

  @CacheEvict(value = "RolePermissions", allEntries = true)
  public void clearAllRolePermissionCache() {
    log.info("Clearing existing permission for all role from memory");
  }
}
