package com.touchblankspot.inventory.portal.web.types.sales;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.UUID;
import lombok.Data;

@Data
public class SalesDetailRequestType {

  @NotNull(message = "Product must be selected")
  private UUID productId;

  @Size(min = 1, max = 20, message = "Product Category must be selected.")
  private String category;

  @NotNull(message = "Product Sub Category must be selected.")
  private UUID subCategory;

  @NotNull(message = "Product Size must be selected")
  @NotEmpty(message = "Product Size must be selected")
  private String size;

  private String unitPrice;

  @NotNull(message = "Product Quantity must be entered.")
  private Double quantity;

  @NotNull(message = "Payment Mode must be selected")
  @NotEmpty(message = "Payment Mode must be selected")
  private String paymentMode;

  private String transactionId;

  private Long currentStock;
}
