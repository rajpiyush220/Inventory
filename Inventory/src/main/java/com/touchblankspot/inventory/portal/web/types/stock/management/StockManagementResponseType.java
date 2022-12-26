package com.touchblankspot.inventory.portal.web.types.stock.management;

import com.touchblankspot.inventory.portal.web.util.CommonUtil;
import java.util.UUID;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class StockManagementResponseType {

  private UUID id;

  private String name;

  private String shortName;

  private String shortDescription;

  private String categoryName;

  private String subCategory;
  private String productSize;

  private Long quantity;

  public StockManagementResponseType(
      UUID id,
      String shortName,
      String name,
      String shortDescription,
      String categoryName,
      String subCategory,
      String productSize,
      Long quantity) {
    this.id = id;
    this.shortName = shortName;
    this.name = name;
    this.shortDescription = shortDescription;
    this.categoryName = categoryName;
    this.subCategory = subCategory;
    this.productSize = productSize;
    this.quantity = quantity;
  }

  public StockManagementResponseType(Object[] objects) {
    this.id = CommonUtil.convertBytesToUUID((byte[]) objects[0]);
    this.name = objects[1].toString();
    this.shortName = objects[2].toString();
    this.shortDescription = objects[3].toString();
    this.productSize = objects[4].toString();
    this.categoryName = objects[5].toString();
    this.subCategory = objects[6].toString();
    this.quantity = Long.valueOf(objects[7].toString());
  }
}
