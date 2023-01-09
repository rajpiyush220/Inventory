package com.touchblankspot.inventory.portal.web.types.product.validator;

import com.touchblankspot.inventory.portal.data.model.Product;
import com.touchblankspot.inventory.portal.data.repository.ProductRepository;
import com.touchblankspot.inventory.portal.service.ProductService;
import com.touchblankspot.inventory.portal.web.types.product.management.ProductUpdateRequestType;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class ProductUpdateValidator implements Validator {

  @NonNull private final ProductService productService;

  @NonNull private final ProductRepository productRepository;

  @Override
  public boolean supports(Class<?> clazz) {
    return ProductUpdateRequestType.class.equals(clazz);
  }

  @Override
  public void validate(Object target, Errors errors) {
    ProductUpdateRequestType requestType = (ProductUpdateRequestType) target;

    Product product = productRepository.findByNameAndIsDeleted(requestType.getName(), false);
    if (product.getId().equals(requestType.getId())) {
      product = null;
    }
    if (product != null) {
      errors.rejectValue("name", "", "Selected product name is already assigned to other product.");
    }

    product = productRepository.findByShortNameAndIsDeleted(requestType.getShortName(), false);
    if (product.getId().equals(requestType.getId())) {
      product = null;
    }
    if (product != null) {
      errors.rejectValue(
          "shortName", "", "Selected product short name is already assigned to other product.");
    }
  }
}
