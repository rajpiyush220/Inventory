package com.touchblankspot.inventory.data.repository;

import com.touchblankspot.inventory.data.model.ProductCategory;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductCategoryRepository extends JpaRepository<ProductCategory, UUID> {}
