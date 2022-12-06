package com.touchblankspot.inventory.portal.service;

import com.touchblankspot.inventory.portal.data.model.ProductCategory;
import com.touchblankspot.inventory.portal.data.repository.ProductCategoryRepository;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
public class ProductCategoryService {
  @NonNull private final ProductCategoryRepository productCategoryRepository;

  public ProductCategory save(ProductCategory productCategory) {
    return productCategoryRepository.save(productCategory);
  }

  public List<ProductCategory> findByProductSize(String productSize) {
    return productCategoryRepository.findByProductSizeAndIsDeleted(productSize, false);
  }

  public List<ProductCategory> findByCategory(String category) {
    return productCategoryRepository.findByCategoryAndIsDeleted(category, false);
  }

  public List<ProductCategory> findBySubCategory(String subCategory) {
    return productCategoryRepository.findBySubCategoryAndIsDeleted(subCategory, false);
  }

  public List<ProductCategory> findAll() {
    return productCategoryRepository.findAllByIsDeleted(false);
  }

  public Page<ProductCategory> findAll(Pageable pageable) {
    return productCategoryRepository.findAll(pageable);
  }

  public ProductCategory findById(UUID id) {
    return productCategoryRepository.findByIdAndIsDeleted(id, false);
  }

  public List<String> findByCategoryContains(String category) {
    return productCategoryRepository.findByCategoryContains(category);
  }

  public List<String> findByProductSizeContains(String productSize) {
    return productCategoryRepository.findByProductSizeContains(productSize);
  }

  public List<String> findBySubCategoryContains(String subCategory) {
    return productCategoryRepository.findBySubCategoryContains(subCategory);
  }

  public void deleteProductCategory(UUID id) {
    ProductCategory productCategory = productCategoryRepository.findByIdAndIsDeleted(id, false);
    if (productCategory != null) {
      productCategory.setIsDeleted(true);
      productCategory.setUpdated(OffsetDateTime.now());
      productCategoryRepository.save(productCategory);
    }
  }
}
