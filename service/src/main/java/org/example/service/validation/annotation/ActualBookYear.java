package org.example.service.validation.annotation;

import org.example.service.validation.annotation.impl.BookYearValidatorImpl;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = BookYearValidatorImpl.class)
public @interface ActualBookYear {
    String message() default "Element is already exist";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

