package com.muhammed.springsecurity.annotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = NameValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidName {
    String message() default "{validation.valid.Name.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
