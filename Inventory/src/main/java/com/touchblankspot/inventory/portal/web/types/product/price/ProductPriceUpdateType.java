package com.touchblankspot.inventory.portal.web.types.product.price;

import com.touchblankspot.inventory.portal.web.util.CommonUtil;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductPriceUpdateType {

  private UUID id;

  private String category;

  private String subCategory;

  private String name;

  private String productSize;

  @Min(value = 1, message = "Product price must be greater than zero.")
  @Max(value = 9999, message = "Product price must be less than 10000.")
  @NotNull(message = "Product price is required.")
  private Double price;

  private String discountPercentage;

  private String maxDiscountAmount;

  public ProductPriceUpdateType(Object[] data) {
    if (data.length > 0) {
      Object[] objects = (Object[]) data[0];
      this.id = CommonUtil.convertBytesToUUID((byte[]) objects[0]);
      this.category = objects[1].toString();
      this.subCategory = objects[2].toString();
      this.name = objects[3].toString();
      this.productSize = objects[4].toString();
      this.price = Double.valueOf(objects[5].toString());
      this.discountPercentage = objects[6].toString();
      this.maxDiscountAmount = objects[7].toString();
    }
  }
}
