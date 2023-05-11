package org.example.service.dto.orderDto;

import lombok.Value;
import lombok.experimental.FieldNameConstants;
import org.example.service.database.entity.OrderStatus;
import org.example.service.database.entity.OrderType;
import org.example.service.validation.annotation.UniqueBookOrder;

import java.time.LocalDateTime;

@Value
@FieldNameConstants
@UniqueBookOrder(message = "This book already ordered")
public class OrderCreateEditDto {

    Long userId;
    Long bookId;
    OrderStatus status;
    OrderType type;
    LocalDateTime orderedDate;
    LocalDateTime returnedDate;
}
