package com.touchblankspot.inventory.portal.web.types.sales;

import com.touchblankspot.inventory.portal.web.util.CommonUtil;
import java.util.UUID;
import lombok.Data;

@Data
public class SalesDetailResponseType {

  private UUID id;
  private String name;

  private String shortName;

  private String shortDescription;

  private String productCategory;

  private String subCategory;

  private String quantity;

  private String unitPrice;

  private String discountAmount;

  private String totalPrice;

  private String paymentMode;

  private String transactionId;

  private String operatorName;

  private String soldAt;

  public SalesDetailResponseType(Object[] objects) {
    this.id = CommonUtil.convertBytesToUUID((byte[]) objects[0]);
    this.name = objects[1].toString();
    this.shortName = objects[2].toString();
    this.shortDescription = objects[3].toString();
    this.productCategory = objects[4].toString();
    this.subCategory = objects[5].toString();
    this.quantity = objects[6].toString();
    this.unitPrice = objects[7].toString();
    this.discountAmount = objects[8].toString();
    this.totalPrice = objects[9].toString();
    this.paymentMode = objects[10].toString();
    this.transactionId = objects[11].toString();
    this.operatorName = objects[12].toString();
    this.soldAt = objects[13].toString();
  }
}
