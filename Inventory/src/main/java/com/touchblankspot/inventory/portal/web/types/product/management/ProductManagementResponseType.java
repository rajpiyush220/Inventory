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

  public ProductManagementResponseType(Object[] objects) {
    this.id = CommonUtil.convertBytesToUUID((byte[]) objects[0]);
    this.shortName = objects[1].toString();
    this.name = objects[2].toString();
    this.shortDescription = objects[3].toString();
    this.material = objects[5].toString();
    this.categoryName = objects[5].toString();
    this.subCategory = objects[6].toString();
  }
}
