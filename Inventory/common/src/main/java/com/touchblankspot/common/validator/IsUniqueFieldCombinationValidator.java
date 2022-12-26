package com.touchblankspot.common.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.util.ObjectUtils;

@Slf4j
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
public class IsUniqueFieldCombinationValidator
    implements ConstraintValidator<IsUniqueFieldCombination, Object> {
  private String firstFieldName;
  private String secondFieldName;

  private String message;

  @NonNull private final ApplicationContext applicationContext;

  private IsUniqueRowExists service;

  @Override
  public void initialize(IsUniqueFieldCombination isUniqueFieldCombination) {
    Class<? extends IsUniqueRowExists> clazz = isUniqueFieldCombination.service();
    this.firstFieldName = isUniqueFieldCombination.first();
    this.secondFieldName = isUniqueFieldCombination.second();
    this.message = isUniqueFieldCombination.message();
    String serviceQualifier = isUniqueFieldCombination.serviceQualifier();

    this.service =
        ObjectUtils.isEmpty(serviceQualifier)
            ? this.applicationContext.getBean(clazz)
            : this.applicationContext.getBean(serviceQualifier, clazz);
  }

  @Override
  public boolean isValid(Object value, ConstraintValidatorContext context) {
    boolean isValid = false;
    try {
      final Object firstObj = BeanUtils.getProperty(value, firstFieldName);
      final Object secondObj = BeanUtils.getProperty(value, secondFieldName);
      isValid =
          !this.service.isUniqueRowCombination(
              firstFieldName, firstObj, secondFieldName, secondObj);
    } catch (final Exception ignore) {
      log.error("Unable to match field value ", ignore);
    }
    if (!isValid) {
      context
          .buildConstraintViolationWithTemplate(message)
          .addPropertyNode(firstFieldName)
          .addConstraintViolation()
          .disableDefaultConstraintViolation();

      context
          .buildConstraintViolationWithTemplate(message)
          .addPropertyNode(secondFieldName)
          .addConstraintViolation()
          .disableDefaultConstraintViolation();
    }
    return isValid;
  }
}
