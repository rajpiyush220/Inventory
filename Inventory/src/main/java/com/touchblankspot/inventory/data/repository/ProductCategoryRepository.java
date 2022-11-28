package com.touchblankspot.inventory.data.repository;

import com.touchblankspot.inventory.data.model.ProductCategory;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductCategoryRepository extends JpaRepository<ProductCategory, UUID> {

    List<ProductCategory> findByProductSize(String productSize);

    List<ProductCategory> findByCategory(String category);

    List<ProductCategory> findBySubCategory(String subCategory);


}
