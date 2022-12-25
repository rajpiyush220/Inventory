package com.touchblankspot.inventory.portal.web.types.stock.management;

import com.touchblankspot.inventory.portal.web.util.CommonUtil;
import java.util.UUID;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class StockAuditResponseType {

  private UUID id;

  private String name;

  private String shortName;

  private String shortDescription;

  private String operationType;

  private Long quantity;

  private String operatedBy;
  private String transactionTime;

  public StockAuditResponseType(Object[] objects) {
    this.id = CommonUtil.convertBytesToUUID((byte[]) objects[0]);
    this.name = objects[1].toString();
    this.shortName = objects[2].toString();
    this.shortDescription = objects[3].toString();
    this.operationType = objects[4].toString();
    this.quantity = Long.valueOf(objects[5].toString());
    this.operatedBy = objects[6].toString();
    this.transactionTime = objects[7] != null ? objects[7].toString() : "";
  }
}
