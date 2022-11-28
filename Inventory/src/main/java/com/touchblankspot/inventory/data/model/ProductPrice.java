package com.touchblankspot.inventory.data.model;

import com.touchblankspot.common.model.embedded.Immutable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrimaryKeyJoinColumn;
import java.math.BigDecimal;
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

  @Column(name = "cost_price")
  private BigDecimal costPrice = BigDecimal.ZERO;

  @Column(name = "logistic_expenses")
  private BigDecimal logisticExpenses = BigDecimal.ZERO;

  @Column(name = "additional_cost")
  private BigDecimal additionalCost = BigDecimal.ZERO;

  @Column(name = "unit_price")
  private BigDecimal unitPrice = BigDecimal.ZERO;

  @Column(name = "sell_price")
  private BigDecimal sellPrice = BigDecimal.ZERO;

  @OneToOne(targetEntity = Product.class, orphanRemoval = true)
  @PrimaryKeyJoinColumn(name = "product_id")
  private Product product;
}
