package com.touchblankspot.inventory.portal.web.types.stock.management;

import com.touchblankspot.inventory.portal.data.enums.StockOperationEnum;
import jakarta.validation.constraints.Min;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockManagementRequestType {

  private UUID productId;

  @Min(value = 1)
  private Long quantity = 0L;

  private StockOperationEnum operationType = StockOperationEnum.SELL;
}
