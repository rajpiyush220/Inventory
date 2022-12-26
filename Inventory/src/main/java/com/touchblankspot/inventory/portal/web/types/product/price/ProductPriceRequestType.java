package com.touchblankspot.inventory.portal.web.types.product.price;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductPriceRequestType {

  @NotNull(message = "Product must be selected.")
  private UUID productId;

  @Size(min = 1, max = 20, message = "Product Category must be selected.")
  private String category;

  @NotNull(message = "Product Sub Category must be selected.")
  private UUID subCategory;

  @Size(min = 1, max = 20, message = "Product Size name must be between 1 and 20 character.")
  private String productSize;

  @Min(value = 1, message = "Product price must be greater than zero.")
  @Max(value = 9999, message = "Product price must be less than 10000.")
  @NotNull(message = "Product price is required.")
  private Double price;

  private String discountPercentage;

  private Long maxDiscountAmount;
}
