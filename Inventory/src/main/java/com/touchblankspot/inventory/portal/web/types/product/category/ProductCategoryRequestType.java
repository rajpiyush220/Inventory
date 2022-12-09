package com.touchblankspot.inventory.portal.web.types.product.category;

import com.touchblankspot.common.validator.UniqueField;
import com.touchblankspot.inventory.portal.service.ProductCategoryService;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductCategoryRequestType {

  @Size(min = 2, max = 50, message = "Category must be between 2 and 50 character.")
  @UniqueField(
      service = ProductCategoryService.class,
      fieldName = "category",
      message = "Selected category already taken.")
  private String category;

  private String subCategory;

  private String productSize;
}
