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
@Constraint(validatedBy = IsUpdatableValidator.class)
@Documented
public @interface IsUpdatable {

  String message() default "Selected Row Already exists";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

  Class<? extends IsUpdatableRow> service();

  String serviceQualifier() default "";

  String first();

  String second();

  String id();
}
