package com.touchblankspot.inventory.portal.web.controller;

import com.touchblankspot.inventory.portal.service.RoleCacheService;
import java.util.List;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RolePermissionController {

  @NonNull private final RoleCacheService roleCacheService;

  @GetMapping("/test")
  public ResponseEntity<List<String>> getData() {
    return ResponseEntity.ok(roleCacheService.getRolePermissions("test"));
  }
}
