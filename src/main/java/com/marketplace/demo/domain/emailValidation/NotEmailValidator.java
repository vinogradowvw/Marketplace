package com.marketplace.demo.domain.emailValidation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import org.apache.commons.validator.routines.EmailValidator;

public class NotEmailValidator implements ConstraintValidator<NotEmail, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isEmpty()) {
            return true;
        }

        EmailValidator validator = EmailValidator.getInstance();
        return !validator.isValid(value);
    }
}
