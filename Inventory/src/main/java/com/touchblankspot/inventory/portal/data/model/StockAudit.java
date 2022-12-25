package com.touchblankspot.inventory.portal.data.model;

import com.touchblankspot.common.data.model.embedded.Immutable;
import com.touchblankspot.inventory.portal.data.enums.StockOperationEnum;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.OffsetDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "stock_audit")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StockAudit extends Immutable {

  @Column(name = "quantity")
  private Long quantity;

  @Column(name = "operation_type")
  @Enumerated(EnumType.STRING)
  private StockOperationEnum operationType;

  @Column(name = "operated_by")
  private String operatedBy;

  @Column(name = "transaction_time")
  private OffsetDateTime transactionTime = OffsetDateTime.now();

  @ManyToOne(targetEntity = Product.class)
  @JoinColumn(name = "product_id")
  private Product product;

  @ManyToOne(targetEntity = Stock.class)
  @JoinColumn(name = "stock_id")
  private Stock stock;
}
