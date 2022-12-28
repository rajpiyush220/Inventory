package com.touchblankspot.inventory.portal.service;

import com.touchblankspot.common.validator.IsUniqueRowExists;
import com.touchblankspot.common.validator.IsUpdatableRow;
import com.touchblankspot.inventory.portal.data.model.Category;
import com.touchblankspot.inventory.portal.data.repository.CategoryRepository;
import com.touchblankspot.inventory.portal.web.types.SelectType;
import com.touchblankspot.inventory.portal.web.types.product.category.CategoryUpdateRequestType;
import java.time.OffsetDateTime;
import java.util.*;
import java.util.stream.Collectors;
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
public class CategoryService implements IsUniqueRowExists, IsUpdatableRow {
  @NonNull private final CategoryRepository productCategoryRepository;

  public Category save(Category productCategory) {
    return productCategoryRepository.save(productCategory);
  }

  public List<Category> findAll() {
    return productCategoryRepository.findAllByIsDeleted(false);
  }

  public Page<Category> findAll(Pageable pageable, String searchType, String searchKey) {
    return productCategoryRepository.findAll(pageable, searchType, searchKey);
  }

  public Category findById(UUID id) {
    return productCategoryRepository.findByIdAndIsDeleted(id, false);
  }

  public List<String> getAutoCompleteSuggestions(String searchType, String searchKey) {
    return (searchType.equalsIgnoreCase("category") || searchType.equalsIgnoreCase("subcategory"))
        ? productCategoryRepository.getAutoCompleteSuggestions(searchType, searchKey)
        : List.of();
  }

  public void deleteProductCategory(UUID id) {
    Category productCategory = productCategoryRepository.findByIdAndIsDeleted(id, false);
    if (productCategory != null) {
      productCategory.setIsDeleted(true);
      productCategory.setUpdated(OffsetDateTime.now());
      productCategoryRepository.save(productCategory);
    }
  }

  public Category updateCategory(CategoryUpdateRequestType requestType) {
    Category category = findById(requestType.getId());
    category.setCategory(requestType.getCategory());
    category.setSubCategory(requestType.getSubCategory());
    return save(category);
  }

  public List<String> getCategoryList() {
    return findAll().stream().map(Category::getCategory).distinct().sorted().toList();
  }

  public List<SelectType> getSubCategorySelectList(String categoryName) {
    return productCategoryRepository.findAllByCategoryAndIsDeleted(categoryName, false).stream()
        .map(
            productCategory ->
                new SelectType(
                    productCategory.getId().toString(), productCategory.getSubCategory()))
        .distinct()
        .sorted(Comparator.comparing(SelectType::value))
        .collect(Collectors.toList());
  }

  @Override
  public boolean isUniqueRowCombination(
      String firstField, Object firstFieldValue, String secondField, Object secondFieldValue)
      throws UnsupportedOperationException {
    List<Category> productCategories =
        getValidationRecords(firstField, firstFieldValue, secondField, secondFieldValue);
    return productCategories != null && productCategories.size() > 0;
  }

  @Override
  public boolean isUpdatableRow(
      String id,
      String firstField,
      Object firstFieldValue,
      String secondField,
      Object secondFieldValue)
      throws UnsupportedOperationException {
    return getValidationRecords(firstField, firstFieldValue, secondField, secondFieldValue).stream()
            .filter(category -> !category.getId().toString().equals(id))
            .count()
        < 1;
  }

  private List<Category> getValidationRecords(
      String firstField, Object firstFieldValue, String secondField, Object secondFieldValue)
      throws UnsupportedOperationException {
    if ("category".equalsIgnoreCase(firstField) && "subCategory".equalsIgnoreCase(secondField)) {
      // Ignoring check if value is empty or its not matching min length criteria
      if (ObjectUtils.isEmpty(firstFieldValue)
          || firstFieldValue.toString().length() < 2
          || ObjectUtils.isEmpty(secondFieldValue)
          || secondFieldValue.toString().length() < 2) {
        return List.of();
      }
      return productCategoryRepository.findByCategoryAndSubCategoryAndIsDeleted(
          firstFieldValue.toString(), secondFieldValue.toString(), false);
    }
    throw new UnsupportedOperationException(
        String.format(
            "Operation not supported for field combination %s and %s" + firstField, secondField));
  }
}
