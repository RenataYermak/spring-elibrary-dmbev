package org.example.service.mapper.orderMapper;

import lombok.RequiredArgsConstructor;
import org.example.service.database.entity.Book;
import org.example.service.database.entity.Order;
import org.example.service.database.entity.OrderStatus;
import org.example.service.database.entity.User;
import org.example.service.database.repository.BookRepository;
import org.example.service.database.repository.UserRepository;
import org.example.service.dto.orderDto.OrderCreateDto;
import org.example.service.mapper.Mapper;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class OrderEditMapper implements Mapper<OrderCreateDto, Order> {

    private final UserRepository userRepository;
    private final BookRepository bookRepository;

    @Override
    public Order map(OrderCreateDto fromObject, Order toObject) {
        copy(fromObject, toObject);
        return toObject;
    }

    @Override
    public Order map(OrderCreateDto object) {
        Order order = new Order();
        copy(object, order);
        return order;
    }

    private void copy(OrderCreateDto object, Order order) {
        order.setUser(getUser(order.getUser().getId()));
        order.setBook(getBook(order.getBook().getId()));
        order.setStatus(OrderStatus.RETURNED);
        order.setType(order.getType());
        order.setOrderedDate(order.getOrderedDate());
        order.setReturnedDate(LocalDateTime.now());
    }

    public Book getBook(Long bookId) {
        return Optional.ofNullable(bookId)
                .flatMap(bookRepository::findById)
                .orElse(null);
    }

    public User getUser(Long userId) {
        return Optional.ofNullable(userId)
                .flatMap(userRepository::findById)
                .orElse(null);
    }

}
