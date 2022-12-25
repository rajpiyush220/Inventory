package com.touchblankspot.inventory.portal.data.model;

import com.touchblankspot.common.data.model.embedded.Mutable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "category")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Category extends Mutable {

  @Column(name = "category")
  @Size(max = 255, min = 1)
  private String category;

  @Column(name = "sub_category", nullable = false)
  @Size(min = 1, max = 255)
  private String subCategory;

  @Column(name = "is_deleted")
  private Boolean isDeleted = false;
}
