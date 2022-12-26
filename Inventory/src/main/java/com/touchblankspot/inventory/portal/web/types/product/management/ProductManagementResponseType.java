package com.touchblankspot.inventory.portal.web.types.product.management;

import com.touchblankspot.inventory.portal.web.util.CommonUtil;
import java.util.UUID;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProductManagementResponseType {

  private UUID id;

  private String shortName;

  private String name;

  private String shortDescription;

  private String description;

  private String material;

  private String discountPercentage;

  private String maxDiscountAmount;

  private String categoryName;

  private String subCategory;
  private String productSize;

  public ProductManagementResponseType(
      UUID id,
      String shortName,
      String name,
      String shortDescription,
      String description,
      String material,
      String discountPercentage,
      String maxDiscountAmount,
      String categoryName,
      String subCategory,
      String productSize) {
    this.id = id;
    this.shortName = shortName;
    this.name = name;
    this.shortDescription = shortDescription;
    this.description = description;
    this.material = material;
    this.discountPercentage = discountPercentage;
    this.maxDiscountAmount = maxDiscountAmount;
    this.categoryName = categoryName;
    this.subCategory = subCategory;
    this.productSize = productSize;
  }

  public ProductManagementResponseType(Object[] objects) {
    this.id = CommonUtil.convertBytesToUUID((byte[]) objects[0]);
    this.shortName = objects[1].toString();
    this.name = objects[2].toString();
    this.shortDescription = objects[3].toString();
    this.description = objects[4].toString();
    this.material = objects[5].toString();
    this.categoryName = objects[6].toString();
    this.subCategory = objects[7].toString();
  }
}
