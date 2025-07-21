package com.itrust.middlewares.nbc.validations.nida.types;

import com.itrust.middlewares.nbc.validations.nida.validators.NinValidator;
import jakarta.validation.Constraint;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = NinValidator.class)
@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface NinValidation {
    String message() default "Invalid NIN";

    Class<?>[] groups() default {};

    Class<?>[] payload() default {};
}
