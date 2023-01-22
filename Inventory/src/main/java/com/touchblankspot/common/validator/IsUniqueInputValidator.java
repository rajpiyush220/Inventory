package com.touchblankspot.common.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.util.ObjectUtils;

@Slf4j
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
public class IsUniqueInputValidator implements ConstraintValidator<IsUniqueInput, Object> {
  private String[] fields;

  private String message;

  private boolean isUpdate;

  @NonNull private final ApplicationContext applicationContext;

  private IsUniqueInputFields service;

  @Override
  public void initialize(IsUniqueInput isUniqueInput) {
    Class<? extends IsUniqueInputFields> clazz = isUniqueInput.service();
    this.fields = isUniqueInput.fieldNames();
    this.message = isUniqueInput.message();
    this.isUpdate = isUniqueInput.isUpdate();
    String serviceQualifier = isUniqueInput.serviceQualifier();

    this.service =
        ObjectUtils.isEmpty(serviceQualifier)
            ? this.applicationContext.getBean(clazz)
            : this.applicationContext.getBean(serviceQualifier, clazz);
  }

  @Override
  public boolean isValid(Object value, ConstraintValidatorContext context) {
    boolean isValid = false;
    try {
      List<String> fieldNames = new ArrayList<>(fields.length);
      List<Object> values = new ArrayList<>(fields.length);
      for (String field : fields) {
        fieldNames.add(field.toLowerCase());
        values.add(BeanUtils.getProperty(value, field));
      }
      isValid = this.service.isUniqueRowCombination(fieldNames, values, isUpdate);
    } catch (final Exception ex) {
      log.error("Unable to match field value ", ex);
    }
    if (!isValid) {
      Arrays.stream(fields)
          .forEach(
              field ->
                  context
                      .buildConstraintViolationWithTemplate(message)
                      .addPropertyNode(field)
                      .addConstraintViolation()
                      .disableDefaultConstraintViolation());
    }
    return isValid;
  }
}
