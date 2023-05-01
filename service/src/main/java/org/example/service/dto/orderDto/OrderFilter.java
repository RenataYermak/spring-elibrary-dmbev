package org.example.service.dto.orderDto;

import lombok.Builder;
import org.example.service.database.entity.OrderStatus;
import org.example.service.database.entity.OrderType;


@Builder
public record OrderFilter(OrderType type,
                          OrderStatus status,
                          String user,
                          String book) {
}
