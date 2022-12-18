package com.touchblankspot.inventory.portal.web.types.sales;

import java.util.UUID;
import lombok.Data;

@Data
public class ProductSaleDetails {

  private UUID productId;

  private String shortName;

  private String name;
}
