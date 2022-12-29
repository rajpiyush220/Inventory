package com.touchblankspot.inventory.portal.service;

import com.touchblankspot.common.validator.IsUniqueInputFields;
import com.touchblankspot.inventory.portal.data.model.Category;
import com.touchblankspot.inventory.portal.data.repository.CategoryRepository;
import com.touchblankspot.inventory.portal.web.types.SelectType;
import com.touchblankspot.inventory.portal.web.types.product.category.CategoryUpdateRequestType;
import java.time.OffsetDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
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
public class CategoryService implements IsUniqueInputFields {
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

  public void updateCategory(CategoryUpdateRequestType requestType) {
    Category category = findById(requestType.getId());
    category.setCategory(requestType.getCategory());
    category.setSubCategory(requestType.getSubCategory());
    save(category);
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
  public boolean isUniqueRowCombination(List<String> fields, List<Object> values, Boolean isUpdate)
      throws UnsupportedOperationException {
    String category = "category";
    String subCategory = "subcategory";
    String id = "id";
    if (!fields.contains(category)
        || !fields.contains(subCategory)
        || (isUpdate && !fields.contains(id))) {
      throw new UnsupportedOperationException(
          "Operation not supported for selected fields " + fields);
    }
    Object categoryValue = values.get(fields.indexOf(category));
    Object subCategoryValue = values.get(fields.indexOf(subCategory));
    if (categoryValue == null
        || categoryValue.toString().length() < 2
        || subCategoryValue == null
        || subCategoryValue.toString().length() < 2) {
      return true;
    }
    List<Category> categories =
        productCategoryRepository.findByCategoryAndSubCategoryAndIsDeleted(
            categoryValue.toString(), subCategoryValue.toString(), false);
    if (isUpdate && fields.contains(id) && values.get(fields.indexOf(id)) != null) {
      UUID categoryId = UUID.fromString(values.get(fields.indexOf(id)).toString());
      categories = categories.stream().filter(data -> !categoryId.equals(data.getId())).toList();
    }
    return categories.size() < 1;
  }
}
