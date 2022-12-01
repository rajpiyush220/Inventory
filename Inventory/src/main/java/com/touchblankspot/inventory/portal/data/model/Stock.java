package com.touchblankspot.inventory.portal.data.model;

import com.touchblankspot.common.data.model.embedded.Immutable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrimaryKeyJoinColumn;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "stock")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Stock extends Immutable {

  @Column(name = "quantity")
  private Long quantity;

  @OneToOne(targetEntity = Product.class)
  @PrimaryKeyJoinColumn(name = "product_id")
  private Product product;
}
