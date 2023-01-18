package com.touchblankspot.common.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

public class UniqueValidator implements ConstraintValidator<UniqueField, Object> {

  @Autowired private ApplicationContext applicationContext;

  private FieldValueExists service;
  private String fieldName;

  @Override
  public void initialize(UniqueField unique) {
    Class<? extends FieldValueExists> clazz = unique.service();
    this.fieldName = unique.fieldName();
    String serviceQualifier = unique.serviceQualifier();

    if (!serviceQualifier.equals("")) {
      this.service = this.applicationContext.getBean(serviceQualifier, clazz);
    } else {
      this.service = this.applicationContext.getBean(clazz);
    }
  }

  @Override
  public boolean isValid(Object o, ConstraintValidatorContext constraintValidatorContext) {
    return !this.service.fieldValueExists(o, this.fieldName);
  }
}
