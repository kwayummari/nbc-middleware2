package com.itrust.middlewares.nbc.validations.nida.types;

import com.itrust.middlewares.nbc.validations.nida.validators.IdTypeValidator;
import jakarta.validation.Constraint;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = IdTypeValidator.class)
@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface IdTypeValidation {
    String message() default "Invalid IdType";

    Class<?>[] groups() default {};

    Class<?>[] payload() default {};
}
