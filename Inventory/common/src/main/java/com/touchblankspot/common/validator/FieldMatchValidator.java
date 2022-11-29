package com.touchblankspot.common.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;

@Slf4j
public class FieldMatchValidator implements ConstraintValidator<FieldMatch, Object> {
  private String firstFieldName;
  private String secondFieldName;
  private String message;

  @Override
  public void initialize(final FieldMatch constraintAnnotation) {
    firstFieldName = constraintAnnotation.first();
    secondFieldName = constraintAnnotation.second();
    message = constraintAnnotation.message();
  }

  @Override
  public boolean isValid(final Object value, final ConstraintValidatorContext context) {
    boolean valid = true;
    try {
      final Object firstObj = BeanUtils.getProperty(value, firstFieldName);
      final Object secondObj = BeanUtils.getProperty(value, secondFieldName);
      valid =
          firstObj == null && secondObj == null || firstObj != null && firstObj.equals(secondObj);
    } catch (final Exception ignore) {
      log.error("Unable to match field value ", ignore);
    }

    if (!valid) {
      context
          .buildConstraintViolationWithTemplate(message)
          .addPropertyNode(firstFieldName)
          .addConstraintViolation()
          .disableDefaultConstraintViolation();
    }

    return valid;
  }
}
