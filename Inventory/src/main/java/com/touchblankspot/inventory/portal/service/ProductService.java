package com.touchblankspot.inventory.portal.service;

import com.touchblankspot.common.validator.FieldValueExists;
import com.touchblankspot.inventory.portal.data.model.Product;
import com.touchblankspot.inventory.portal.data.repository.ProductRepository;
import com.touchblankspot.inventory.portal.web.types.product.management.ProductUpdateRequestType;
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
import org.springframework.util.ObjectUtils;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
public class ProductService implements FieldValueExists {
  @NonNull private final ProductRepository productRepository;

  @NonNull private final CategoryService categoryService;

  @Override
  public boolean fieldValueExists(Object value, String fieldName)
      throws UnsupportedOperationException {
    if (ObjectUtils.isEmpty(value) || value.toString().length() < 2) {
      return false;
    }
    if ("name".equalsIgnoreCase(fieldName)) {
      return productRepository.findByNameAndIsDeleted(value.toString(), false) != null;
    }
    if ("shortName".equalsIgnoreCase(fieldName)) {
      return productRepository.findByShortNameAndIsDeleted(value.toString(), false) != null;
    }
    throw new UnsupportedOperationException("Operation not supported for " + fieldName);
  }

  public Product save(Product product) {
    return productRepository.save(product);
  }

  public Page<Object[]> getListData(Pageable pageable, String searchType, String searchKey) {
    return productRepository.getListData(pageable, searchType, searchKey);
  }

  public List<String> getAutoCompleteSuggestions(String searchType, String searchKey) {
    return productRepository.getAutoCompleteSuggestions(searchType, searchKey);
  }

  public Product findById(UUID id) {
    Product product =
        productRepository
            .findById(id)
            .orElseThrow(() -> new RuntimeException("No product found with id " + id));
    product.setCategory(categoryService.findById(product.getCategoryId()));
    return product;
  }

  public List<Product> findByIdCategoryId(UUID id) {
    return productRepository.findByCategoryIdAndIsDeleted(id, false);
  }

  public void deleteProduct(UUID id) {
    Product product = productRepository.findByIdAndIsDeleted(id, false);
    if (product != null) {
      product.setIsDeleted(true);
      product.setUpdated(OffsetDateTime.now());
      productRepository.save(product);
    }
  }

  public void updateProduct(ProductUpdateRequestType requestType) {
    Product product = findById(requestType.getId());
    product.setName(requestType.getName());
    product.setShortName(requestType.getShortName());
    product.setShortDescription(requestType.getShortDescription());
    product.setDescription(requestType.getDescription());
    product.setMaterial(requestType.getMaterial());
    save(product);
  }
}
