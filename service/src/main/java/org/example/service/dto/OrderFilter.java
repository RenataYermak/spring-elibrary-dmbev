package org.example.service.dto;

import lombok.Builder;
import lombok.Value;
import org.example.service.database.entity.OrderStatus;
import org.example.service.database.entity.OrderType;

import java.time.LocalDateTime;

@Value
@Builder
public class OrderFilter {

    OrderType type;
    OrderStatus status;
    String user;
    String book;
    LocalDateTime orderedDate;
}
