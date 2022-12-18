package com.touchblankspot.inventory.portal.service;

import com.touchblankspot.common.validator.FieldValueExists;
import com.touchblankspot.inventory.portal.data.model.Category;
import com.touchblankspot.inventory.portal.data.repository.CategoryRepository;
import com.touchblankspot.inventory.portal.web.types.SelectType;
import java.time.OffsetDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
public class CategoryService implements FieldValueExists {
  @NonNull private final CategoryRepository productCategoryRepository;

  public Category save(Category productCategory) {
    return productCategoryRepository.save(productCategory);
  }

  public List<Category> findByCategory(String category) {
    return productCategoryRepository.findByCategoryAndIsDeleted(category, false);
  }

  public List<Category> findBySubCategory(String subCategory) {
    return productCategoryRepository.findBySubCategoryAndIsDeleted(subCategory, false);
  }

  public List<Category> findAll() {
    return productCategoryRepository.findAllByIsDeleted(false);
  }

  public Page<Category> findAll(Pageable pageable) {
    return productCategoryRepository.findAll(pageable);
  }

  public Category findById(UUID id) {
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
    Category productCategory = productCategoryRepository.findByIdAndIsDeleted(id, false);
    if (productCategory != null) {
      productCategory.setIsDeleted(true);
      productCategory.setUpdated(OffsetDateTime.now());
      productCategoryRepository.save(productCategory);
    }
  }

  public List<SelectType> getCategorySelectList() {
    return findAll().stream()
        .sorted(Comparator.comparing(Category::getCategory))
        .map(
            productCategory ->
                new SelectType(productCategory.getId().toString(), productCategory.getCategory()))
        .toList();
  }

  @Override
  public boolean fieldValueExists(Object value, String fieldName)
      throws UnsupportedOperationException {
    if ("category".equalsIgnoreCase(fieldName)) {
      // Ignoring check if value is empty or its not matching min length criteria
      if (ObjectUtils.isEmpty(value) || value.toString().length() < 2) {
        return false;
      }
      List<Category> productCategories =
          productCategoryRepository.findByCategoryAndIsDeleted(value.toString(), false);
      return productCategories != null && productCategories.size() > 0;
    }
    throw new UnsupportedOperationException("Operation not supported for " + fieldName);
  }
}
