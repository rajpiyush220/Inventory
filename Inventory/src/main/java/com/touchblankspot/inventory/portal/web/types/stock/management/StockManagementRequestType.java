package com.touchblankspot.inventory.portal.web.types.stock.management;

import com.touchblankspot.inventory.portal.data.enums.StockOperationEnum;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockManagementRequestType {

  @NotNull(message = "Product must be selected")
  private UUID productId;

  @Size(min = 1, max = 20, message = "Product Category must be selected.")
  private String category;

  @NotNull(message = "Product Sub Category must be selected.")
  private UUID subCategory;

  @Min(value = 1)
  private Long quantity = 0L;

  @NotNull(message = "Product Size must be selected")
  @NotEmpty(message = "Product Size must be selected")
  private String productSize;

  private Long currentStock = 0L;

  private StockOperationEnum operationType = StockOperationEnum.PURCHASE;
}
