package com.touchblankspot.inventory.portal.service;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
public class PermissionService {

  @NonNull private final RoleCacheService roleCacheService;

  public Boolean hasPermission(Object permissions) {
    Authentication authentication = getAuthentication();
    if (!authentication.isAuthenticated()) {
      return false;
    }
    Set<String> userPermissionSet = getCurrentUserPermissions();
    Collection permissionsToCheck =
        permissions instanceof Collection
            ? (Collection) permissions
            : Collections.singleton(permissions);
    return CollectionUtils.containsAny(permissionsToCheck, userPermissionSet);
  }

  public Boolean hasPermissions(List<String> permissionsToCheck) {
    Authentication authentication = getAuthentication();
    if (!authentication.isAuthenticated()) {
      return false;
    }
    return getCurrentUserPermissions().containsAll(permissionsToCheck);
  }

  public Set<String> getCurrentUserPermissions() {
    return getAuthentication().getAuthorities().stream()
        .map(
            grantedAuthority ->
                roleCacheService.getRolePermissions(grantedAuthority.getAuthority()))
        .flatMap(Collection::stream)
        .collect(Collectors.toSet());
  }

  public Authentication getAuthentication() {
    return SecurityContextHolder.getContext().getAuthentication();
  }
}
