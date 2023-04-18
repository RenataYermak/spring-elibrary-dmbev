package org.example.service.mapper.orderMapper;

import lombok.RequiredArgsConstructor;
import org.example.service.database.entity.Book;
import org.example.service.database.entity.Order;
import org.example.service.database.entity.User;
import org.example.service.database.repository.BookRepository;
import org.example.service.database.repository.UserRepository;
import org.example.service.dto.orderDto.OrderCreateEditDto;
import org.example.service.mapper.Mapper;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class OrderCreateEditMapper implements Mapper<OrderCreateEditDto, Order> {

    private final UserRepository userRepository;
    private final BookRepository bookRepository;

    @Override
    public Order map(OrderCreateEditDto fromObject, Order toObject) {
        copy(fromObject, toObject);
        return toObject;
    }

    @Override
    public Order map(OrderCreateEditDto object) {
        Order order = new Order();
        copy(object, order);
        return order;
    }

    private void copy(OrderCreateEditDto object, Order order) {
        order.setUser(getUser(object.getUserId()));
        order.setBook(getBook(object.getBookId()));
        order.setStatus(object.getStatus());
        order.setType(object.getType());
        order.setOrderedDate(object.getOrderedDate());
        order.setReturnedDate(object.getReturnedDate());
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
