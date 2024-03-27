package com.muhammed.springsecurity.annotations;

import com.muhammed.springsecurity.user.dataAccess.abstracts.UserDao;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, String> {

    private final UserDao userDao;

    public UniqueEmailValidator(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        return !userDao.existsUserByEmail(value);
    }
}
