package com.touchblankspot.inventory.portal.data.enums;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum PortalRolePermission {
  PROD_CAT_CREATE("Create Product Category"),
  PROD_CAT_VIEW("View Product Category"),
  PROD_CAT_UPDATE("Update Product Category"),
  PROD_CAT_DELETE("Delete Product Category");

  private final String description;

  private static final Map<String, PortalRolePermission> enumMap =
      Collections.unmodifiableMap(
          Arrays.stream(PortalRolePermission.values())
              .collect(Collectors.toMap(PortalRolePermission::name, Function.identity())));

  public static PortalRolePermission fromString(String permission) {
    return enumMap.get(permission);
  }
}
