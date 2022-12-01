package com.touchblankspot.inventory.portal.web.types.product.category;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductCategoryRequestType {

  @Size(min = 2, max = 50, message = "Category must be between 2 and 50 character.")
  private String category;

  private String subCategory;

  private String productSize;
}
