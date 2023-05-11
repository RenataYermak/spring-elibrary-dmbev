package org.example.service.dto.orderDto;

import lombok.Value;
import lombok.experimental.FieldNameConstants;
import org.example.service.database.entity.OrderStatus;
import org.example.service.database.entity.OrderType;
import org.example.service.dto.bookDto.BookReadDto;
import org.example.service.dto.userDto.UserReadDto;

import java.time.LocalDateTime;

@Value
@FieldNameConstants
public class OrderReadDto {
    Long id;
    UserReadDto user;
    BookReadDto book;
    OrderStatus status;
    OrderType type;
    LocalDateTime ordered_date;
    LocalDateTime returned_date;
}
