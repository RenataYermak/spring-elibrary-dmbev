package org.example.service.dto.orderDto;

import lombok.Value;
import org.example.service.database.entity.OrderStatus;
import org.example.service.database.entity.OrderType;

import java.time.LocalDateTime;

@Value
public class OrderCreateEditDto {

    Long userId;
    Long bookId;
    OrderStatus status;
    OrderType type;
    LocalDateTime orderedDate;
    LocalDateTime returnedDate;
}
