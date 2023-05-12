package org.example.service.validation.annotation.impl;

import org.example.service.validation.annotation.ActualBookYear;
import org.example.service.validation.annotation.BookYearValidator;

import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class BookYearValidatorImpl implements BookYearValidator {

    @Override
    public void initialize(ActualBookYear constraintAnnotation) {
        BookYearValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        int currentYear = LocalDate.now().getYear();
        return value <= currentYear;
    }
}
