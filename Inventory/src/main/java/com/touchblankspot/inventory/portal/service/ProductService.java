package com.touchblankspot.inventory.portal.service;

import com.touchblankspot.common.validator.FieldValueExists;
import com.touchblankspot.inventory.portal.data.model.Product;
import com.touchblankspot.inventory.portal.data.repository.ProductRepository;
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
public class ProductService implements FieldValueExists {
  @NonNull private final ProductRepository productRepository;

  @Override
  public boolean fieldValueExists(Object value, String fieldName)
      throws UnsupportedOperationException {
    // Ignoring check if value is empty or its not matching min length criteria
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

  public Page<Product> findAll(Pageable pageable) {
    return productRepository.findAll(pageable);
  }

  public Page<Object[]> getListData(Pageable pageable) {
    return productRepository.getListData(pageable);
  }

  public List<SelectType> getProductSelectList() {
    return productRepository.findAll().stream()
        .sorted(Comparator.comparing(Product::getName))
        .map(product -> new SelectType(product.getId().toString(), product.getName()))
        .toList();
  }

  public Product findById(UUID id) {
    return productRepository
        .findById(id)
        .orElseThrow(() -> new RuntimeException("No product found with id " + id));
  }

  public void deleteProduct(UUID id) {
    Product product = productRepository.findByIdAndIsDeleted(id, false);
    if (product != null) {
      product.setIsDeleted(true);
      product.setUpdated(OffsetDateTime.now());
      productRepository.save(product);
    }
  }
}
