package org.example.service.validation.annotation;

import org.example.service.validation.annotation.impl.UniqueBookValidatorImpl;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UniqueBookValidatorImpl.class)
public @interface UniqueBookOrder {
    String message() default "Element is already exist";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default {};
}

