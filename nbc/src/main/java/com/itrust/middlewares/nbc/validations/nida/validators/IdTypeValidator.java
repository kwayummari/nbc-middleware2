package com.itrust.middlewares.nbc.validations.nida.validators;

import com.itrust.middlewares.nbc.validations.nida.types.IdTypeValidation;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class IdTypeValidator implements ConstraintValidator<IdTypeValidation, String> {

    @Override
    public void initialize(IdTypeValidation ninValidation) {}

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        // Define your custom validation logic here

        return value.equals("NIDARQ");
    }
}