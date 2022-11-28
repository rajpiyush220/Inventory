package com.touchblankspot.inventory.data.model;

import com.touchblankspot.common.model.embedded.Immutable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "sales_details")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SalesDetails extends Immutable {

  @Column(name = "quantity")
  private Long quantity;

  @Column(name = "unit_price")
  private BigDecimal unitPrice = BigDecimal.ZERO;

  @Column(name = "discount_amount")
  private BigDecimal discountAmount = BigDecimal.ZERO;

  @Column(name = "total_price")
  private BigDecimal totalPrice = BigDecimal.ZERO;

  @Column(name = "payment_mode")
  private String paymentMode;

  @Column(name = "transaction_id")
  private String transactionId;

  @Column(name = "sold_by")
  private String soldBy;

  @Column(name = "sold_at")
  private OffsetDateTime soldAt = OffsetDateTime.now();

  @ManyToOne(targetEntity = Product.class)
  @JoinColumn(name = "product_id")
  private Product product;
}
