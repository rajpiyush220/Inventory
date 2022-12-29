package com.touchblankspot.common.validator;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Target({TYPE, ANNOTATION_TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = IsUniqueInputValidator.class)
@Documented
public @interface IsUniqueInput {
  String message() default "Selected fields must have unique rows";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

  Class<? extends IsUniqueInputFields> service();

  String serviceQualifier() default "";

  boolean isUpdate() default false;

  String[] fieldNames();
}
