package com.touchblankspot.inventory.portal.data.model;

import com.touchblankspot.common.data.model.embedded.Mutable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "product")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Product extends Mutable {

  @Column(name = "short_name")
  private String shortName;

  @Column(name = "name")
  private String name;

  @Column(name = "short_description")
  private String shortDescription;

  @Column(name = "description")
  private String description;

  @Column(name = "material")
  private String material;

  @Column(name = "discount_percentage")
  private String discountPercentage;

  @Column(name = "max_discount_amount")
  private Long maxDiscountAmount = 0L;

  @ManyToOne(targetEntity = ProductCategory.class)
  @JoinColumn(name = "category_id")
  private ProductCategory productCategory;

  @OneToOne(mappedBy = "product", targetEntity = ProductPrice.class)
  private ProductPrice productPrice;

  @OneToOne(mappedBy = "product", targetEntity = Stock.class)
  private Stock stock;
}
