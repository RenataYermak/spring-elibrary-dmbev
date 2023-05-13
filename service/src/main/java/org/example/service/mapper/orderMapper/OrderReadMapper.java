package org.example.service.mapper.orderMapper;

import lombok.RequiredArgsConstructor;
import org.example.service.database.entity.Order;
import org.example.service.dto.orderDto.OrderReadDto;
import org.example.service.mapper.Mapper;
import org.example.service.mapper.bookMapper.BookReadMapper;
import org.example.service.mapper.userMapper.UserReadMapper;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class OrderReadMapper implements Mapper<Order, OrderReadDto> {

    private final UserReadMapper userReadMapper;
    private final BookReadMapper bookReadMapper;

    @Override
    public OrderReadDto map(Order object) {
        var user = Optional.ofNullable(object.getUser())
                .map(userReadMapper::map)
                .orElse(null);
        var book = Optional.ofNullable(object.getBook())
                .map(bookReadMapper::map)
                .orElse(null);

        return new OrderReadDto(
                object.getId(),
                user,
                book,
                object.getStatus(),
                object.getType(),
                object.getOrderedDate(),
                object.getReturnedDate()
        );
    }
}
