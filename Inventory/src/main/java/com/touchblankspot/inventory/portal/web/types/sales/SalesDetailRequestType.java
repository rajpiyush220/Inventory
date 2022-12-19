package com.touchblankspot.inventory.portal.web.types.sales;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Data;

@Data
public class SalesDetailRequestType {

  @NotNull(message = "Product must be selected")
  private UUID productId;

  @NotNull(message = "Product Size must be selected")
  @NotEmpty(message = "Product Size must be selected")
  private String size;

  private String unitPrice;

  @NotNull(message = "Product Quantity must be selected")
  private Double quantity;

  @NotNull(message = "Payment Mode must be selected")
  @NotEmpty(message = "Payment Mode must be selected")
  private String paymentMode;

  private String transactionId;

}
