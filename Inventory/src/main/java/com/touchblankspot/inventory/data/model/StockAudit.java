package com.touchblankspot.inventory.data.model;

import com.touchblankspot.common.data.model.embedded.Immutable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.OffsetDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "stock_audit")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StockAudit extends Immutable {

  @Column(name = "quantity")
  private Long quantity;

  @Column(name = "operation_type")
  private String operationType;

  @Column(name = "operated_by")
  private String operatedBy;

  @Column(name = "transaction_time")
  private OffsetDateTime transactionTime = OffsetDateTime.now();

  @ManyToOne(targetEntity = Product.class)
  @JoinColumn(name = "product_id")
  private Product product;
}
