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
public class IsUpdatableValidator implements ConstraintValidator<IsUpdatable, Object> {
  private String firstFieldName;
  private String secondFieldName;

  private String message;

  private String id;

  @NonNull private final ApplicationContext applicationContext;

  private IsUpdatableRow service;

  @Override
  public void initialize(IsUpdatable isEligibleForUpdate) {
    Class<? extends IsUpdatableRow> clazz = isEligibleForUpdate.service();
    this.firstFieldName = isEligibleForUpdate.first();
    this.secondFieldName = isEligibleForUpdate.second();
    this.message = isEligibleForUpdate.message();
    String serviceQualifier = isEligibleForUpdate.serviceQualifier();
    this.id = isEligibleForUpdate.id();

    this.service =
        ObjectUtils.isEmpty(serviceQualifier)
            ? this.applicationContext.getBean(clazz)
            : this.applicationContext.getBean(serviceQualifier, clazz);
  }

  @Override
  public boolean isValid(Object value, ConstraintValidatorContext context) {
    boolean isValid = true;
    try {
      final Object firstObj = BeanUtils.getProperty(value, firstFieldName);
      final Object secondObj = BeanUtils.getProperty(value, secondFieldName);
      final Object idValue = BeanUtils.getProperty(value, id);
      isValid =
          this.service.isUpdatableRow(
              idValue.toString(), firstFieldName, firstObj, secondFieldName, secondObj);
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
