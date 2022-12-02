package com.touchblankspot.inventory.portal.web.types.product.category;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductCategoryResponseType {

  private UUID id;

  private String category;

  private String subCategory;

  private String productSize;
}
