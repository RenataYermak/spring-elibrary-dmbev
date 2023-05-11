package org.example.service.validation.annotation.impl;

import lombok.RequiredArgsConstructor;
import org.example.service.validation.annotation.UniqueValidator;
import org.example.service.validation.annotation.Unique;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.validation.ConstraintValidatorContext;

@Component
@RequiredArgsConstructor
public class UniqueValidatorImpl implements UniqueValidator {

    private String fieldName;
    private String entityName;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void initialize(Unique constraintAnnotation) {
        this.fieldName = constraintAnnotation.fieldName();
        this.entityName = constraintAnnotation.entityName();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        String query = ("SELECT COUNT(*) > 0 FROM " + entityName + " WHERE " + fieldName + " = :value");
        TypedQuery<Boolean> typedQuery = entityManager.createQuery(query, Boolean.class);
        typedQuery.setParameter("value", value);
        return !typedQuery.getSingleResult();
    }
}
