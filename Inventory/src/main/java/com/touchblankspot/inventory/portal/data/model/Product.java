package com.touchblankspot.inventory.portal.data.model;

import com.touchblankspot.common.data.model.embedded.Mutable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import java.util.UUID;

import jakarta.persistence.Transient;
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

  @Column(name = "is_deleted")
  private Boolean isDeleted = false;

  @Column(name = "category_id")
  private UUID categoryId;

  @Transient
  private Category category;
}
