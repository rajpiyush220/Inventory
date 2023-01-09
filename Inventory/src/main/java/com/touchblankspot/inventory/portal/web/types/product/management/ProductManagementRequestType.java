package com.touchblankspot.inventory.portal.web.types.product.management;

import com.touchblankspot.common.validator.UniqueField;
import com.touchblankspot.inventory.portal.service.ProductService;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductManagementRequestType {

  @Size(min = 2, max = 50, message = "Product short name must be between 2 and 50 character.")
  @UniqueField(
      service = ProductService.class,
          fieldName = "shortName",
      message = "Selected product shortName is already taken.")
  private String shortName;

  @Size(min = 2, max = 50, message = "Product name must be between 2 and 50 character.")
  @UniqueField(
      service = ProductService.class,
          fieldName = "name",
      message = "Selected product name is already taken.")
  private String name;

  @Size(min = 1, max = 50, message = "Category name must be Selected.")
  @NotNull
  private String categoryName;

  @NotNull(message = "Product Sub category must be selected.")
  private UUID categoryId;

  @Size(min = 2, max = 50, message = "Product shortDescription must be between 2 and 50 character.")
  private String shortDescription;

  @Size(min = 2, max = 255, message = "Product description must be between 2 and 255 character.")
  private String description;

  @Size(min = 2, max = 50, message = "Product material must be between 2 and 50 character.")
  private String material;
}
