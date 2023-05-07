package org.example.service.mapper.orderMapper;

import lombok.RequiredArgsConstructor;
import org.example.service.database.entity.Book;
import org.example.service.database.entity.Order;
import org.example.service.database.entity.OrderStatus;
import org.example.service.database.entity.User;
import org.example.service.database.repository.BookRepository;
import org.example.service.database.repository.UserRepository;
import org.example.service.dto.orderDto.OrderCreateEditDto;
import org.example.service.mapper.Mapper;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class OrderCreateMapper implements Mapper<OrderCreateEditDto, Order> {

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
        order.setUser(getUser(object.getUserId()).get());
        order.setBook(getBook(object.getBookId()).get());
        order.setStatus(OrderStatus.ORDERED);
        order.setType(object.getType());
        order.setOrderedDate(LocalDateTime.now());
        order.setReturnedDate(object.getReturnedDate());
    }

    public Optional<Book> getBook(Long bookId) {
        return Optional.ofNullable(bookId)
                .flatMap(bookRepository::findById);
    }

    public Optional<User> getUser(Long userId) {
        return Optional.ofNullable(userId)
                .flatMap(userRepository::findById);
    }
}
