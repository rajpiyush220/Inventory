package com.touchblankspot.inventory.data.model;

import com.touchblankspot.common.model.embedded.Mutable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "product_category")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductCategory extends Mutable {

  @Column(name = "category")
  @Size(max = 255, min = 1)
  private String category;

  @Column(name = "sub_category")
  @Size(max = 255, min = 1)
  private String subCategory;

  @Column(name = "product_size")
  @Size(max = 20, min = 1)
  private String productSize;

  @OneToMany(mappedBy = "productCategory")
  Set<Product> products = new HashSet<>(0);
}
