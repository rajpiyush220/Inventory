package com.touchblankspot.inventory.portal.web.types.product.category;

import com.touchblankspot.common.validator.IsUniqueInput;
import com.touchblankspot.inventory.portal.service.CategoryService;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@IsUniqueInput(
    service = CategoryService.class,
    fieldNames = {"category", "subCategory"},
    message = "Selected category and subcategory pair is already taken.")
public class CategoryRequestType {

  private String existingCategories;

  private String existingSubCategories;

  @Size(min = 2, max = 50, message = "Category must be between 2 and 50 character.")
  private String category;

  @Size(min = 2, max = 50, message = "SubCategory must be between 2 and 50 character.")
  private String subCategory;
}
