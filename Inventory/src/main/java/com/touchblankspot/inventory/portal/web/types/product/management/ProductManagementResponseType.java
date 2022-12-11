package com.touchblankspot.inventory.portal.web.types.product.management;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductManagementResponseType {

  private UUID id;

  private String category;

  private String subCategory;

  private String productSize;
}
