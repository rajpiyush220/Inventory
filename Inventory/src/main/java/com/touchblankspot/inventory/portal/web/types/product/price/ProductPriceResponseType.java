package com.touchblankspot.inventory.portal.web.types.product.price;

import com.touchblankspot.inventory.portal.web.util.CommonUtil;
import java.util.UUID;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProductPriceResponseType {

  private UUID id;

  private String shortName;
  private String name;

  private String shortDescription;

  private String material;

  private String productSize;

  private String productPrice;

  private String discountPercentage;

  private String maxDiscountAmount;

  public ProductPriceResponseType(Object[] objects) {
    this.id = CommonUtil.convertBytesToUUID((byte[]) objects[0]);
    this.shortName = objects[1].toString();
    this.name = objects[2].toString();
    this.shortDescription = objects[3].toString();
    this.material = objects[4].toString();
    this.productSize = objects[5].toString();
    this.productPrice = objects[6].toString();
    this.discountPercentage = objects[7].toString();
    this.maxDiscountAmount = objects[8].toString();
  }
}
