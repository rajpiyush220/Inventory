package com.touchblankspot.inventory.portal.web.types.product.category;

import com.touchblankspot.common.validator.IsUpdatable;
import com.touchblankspot.inventory.portal.service.CategoryService;
import jakarta.validation.constraints.Size;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@IsUpdatable(
    service = CategoryService.class,
    first = "category",
    second = "subCategory",
    id = "id",
    message = "Selected category and subcategory pair is already taken.")
public class CategoryUpdateRequestType {

  private UUID id;

  @Size(min = 2, max = 50, message = "Category must be between 2 and 50 character.")
  private String category;

  @Size(min = 2, max = 50, message = "SubCategory must be between 2 and 50 character.")
  private String subCategory;
}
