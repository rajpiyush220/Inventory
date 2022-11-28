package com.touchblankspot.common.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.lang.reflect.Field;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class EqualFieldsValidator implements ConstraintValidator<EqualFields, Object> {

  private String baseField;
  private String matchField;

  @Override
  public void initialize(EqualFields constraint) {
    baseField = constraint.baseField();
    matchField = constraint.matchField();
  }

  @Override
  public boolean isValid(Object object, ConstraintValidatorContext context) {
    try {
      Object baseFieldValue = getFieldValue(object, baseField);
      Object matchFieldValue = getFieldValue(object, matchField);
      return baseFieldValue != null && baseFieldValue.equals(matchFieldValue);
    } catch (Exception e) {
      log.error("Unable to match field value ", e);
      return false;
    }
  }

  private Object getFieldValue(Object object, String fieldName) throws Exception {
    try {
      Class<?> clazz = object.getClass();
      Field passwordField = clazz.getDeclaredField(fieldName);
      passwordField.setAccessible(true);
      return passwordField.get(object);
    } catch (Exception ex) {
      log.error(ex.getLocalizedMessage());
      return null;
    }
  }
}
