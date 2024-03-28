package com.muhammed.springsecurity.annotations;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Objects;

public class NameValidator implements ConstraintValidator<PasswordPattern, String> {

    @Override
    public boolean isValid(String name, ConstraintValidatorContext context) {
        return Objects.nonNull(name) && name.length() >= 2;
    }

}