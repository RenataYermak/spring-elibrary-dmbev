package org.example.service.validation.annotation;

import javax.validation.ConstraintValidator;

public interface BookYearValidator extends ConstraintValidator<ActualBookYear, Integer> {
}
