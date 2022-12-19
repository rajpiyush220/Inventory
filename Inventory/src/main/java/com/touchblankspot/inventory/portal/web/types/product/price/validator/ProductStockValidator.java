package com.touchblankspot.inventory.portal.web.types.product.price.validator;

import com.touchblankspot.inventory.portal.web.types.sales.SalesDetailRequestType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class ProductStockValidator implements Validator {
  @Override
  public boolean supports(Class<?> clazz) {
    return SalesDetailRequestType.class.equals(clazz);
  }

  @Override
  public void validate(Object target, Errors errors) {
    SalesDetailRequestType request = (SalesDetailRequestType) target;
    if (request.getProductId() != null
        && request.getSize() != null
        && request.getSize() != ""
        && request.getQuantity().longValue() > 0) {}
  }
}
