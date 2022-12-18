package com.touchblankspot.inventory.portal.data.model;

import com.touchblankspot.common.data.model.embedded.Immutable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import java.math.BigDecimal;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "product_price")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductPrice extends Immutable {

  @Column(name = "product_id")
  private UUID productId;

  @Column(name = "product_size")
  private String productSize;

  @Column(name = "price")
  private BigDecimal price = BigDecimal.ZERO;

  @Column(name = "discount_percentage")
  private String discountPercentage;

  @Column(name = "max_discount_amount")
  private Long maxDiscountAmount = 0L;
}
