package com.touchblankspot.inventory.portal.web.types.product.category;

import com.touchblankspot.common.validator.IsUniqueFieldCombination;
import com.touchblankspot.inventory.portal.service.CategoryService;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@IsUniqueFieldCombination(
    service = CategoryService.class,
    first = "category",
    second = "subCategory",
    message = "Selected category and subcategory pair is already taken.")
public class CategoryRequestType {

  private String existingCategories;

  private String existingSubCategories;

  @Size(min = 2, max = 50, message = "Category must be between 2 and 50 character.")
  private String category;

  @Size(min = 2, max = 50, message = "SubCategory must be between 2 and 50 character.")
  private String subCategory;
}
