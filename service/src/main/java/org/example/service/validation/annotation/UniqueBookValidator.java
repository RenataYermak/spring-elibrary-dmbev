package org.example.service.validation.annotation;

import javax.validation.ConstraintValidator;

public interface UniqueBookValidator extends ConstraintValidator<UniqueBookOrder, Object> {
}
