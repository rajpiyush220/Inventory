package com.touchblankspot.inventory.portal.data.model;

import com.touchblankspot.common.data.model.embedded.Mutable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "stock")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Stock extends Mutable {

  @Column(name = "quantity")
  private Long quantity;

  @Column(name = "product_id")
  private UUID productId;

  @OneToMany(targetEntity = StockAudit.class, fetch = FetchType.EAGER)
  @JoinColumn(name = "id")
  private Set<StockAudit> stockAudits = new HashSet<>(0);
}
