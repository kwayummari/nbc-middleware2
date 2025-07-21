package com.itrust.middlewares.nbc.validations.nida.validators;

import com.itrust.middlewares.nbc.validations.nida.types.NinValidation;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class NinValidator implements ConstraintValidator<NinValidation, String> {

    @Override
    public void initialize(NinValidation ninValidation) {}

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        // Define your custom validation logic here

        if(value == null) {
            return false;
        }

        return value.length() == 20;
    }
}