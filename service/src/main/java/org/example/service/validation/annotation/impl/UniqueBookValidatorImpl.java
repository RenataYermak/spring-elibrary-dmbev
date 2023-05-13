package org.example.service.validation.annotation.impl;

import lombok.RequiredArgsConstructor;
import org.example.service.database.repository.OrderRepository;
import org.example.service.dto.orderDto.OrderCreateEditDto;
import org.example.service.validation.annotation.UniqueBookOrder;
import org.example.service.validation.annotation.UniqueBookValidator;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidatorContext;

@Component
@RequiredArgsConstructor
public class UniqueBookValidatorImpl implements UniqueBookValidator {

    private final OrderRepository orderRepository;

    @Override
    public void initialize(UniqueBookOrder constraintAnnotation) {
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        var dto = (OrderCreateEditDto) value;
        long bookIdValue = dto.getBookId();
        long userIdValue = dto.getUserId();

        var orderId = orderRepository.findByUserIdAndBookId(userIdValue, bookIdValue);
        return orderId.isEmpty();
    }
}
