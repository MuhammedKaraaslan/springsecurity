package com.muhammed.springsecurity.annotations;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordPatternValidator implements ConstraintValidator<PasswordPattern, String> {

//    Contains at least one lowercase letter.
//    Contains at least one uppercase letter.
//    Contains at least one digit.
//    Contains at least one special character.
//    Has a minimum length of 8 characters.
    private static final String PASSWORD_PATTERN = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^\\da-zA-Z]).{8,}$";

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        return (password != null) && password.matches(PASSWORD_PATTERN);
    }
}