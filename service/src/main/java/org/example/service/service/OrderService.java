package org.example.service.service;

import lombok.RequiredArgsConstructor;
import org.example.service.database.entity.Book;
import org.example.service.database.querydsl.QPredicates;
import org.example.service.database.repository.BookRepository;
import org.example.service.database.repository.OrderRepository;
import org.example.service.dto.orderDto.OrderCreateDto;
import org.example.service.dto.orderDto.OrderFilter;
import org.example.service.dto.orderDto.OrderReadDto;
import org.example.service.mapper.orderMapper.OrderCreateMapper;
import org.example.service.mapper.orderMapper.OrderEditMapper;
import org.example.service.mapper.orderMapper.OrderReadMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.example.service.database.entity.QOrder.order;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {

    private final BookRepository bookRepository;
    private final OrderRepository orderRepository;
    private final OrderReadMapper orderReadMapper;
    private final OrderCreateMapper orderCreateEditMapper;
    private final OrderEditMapper orderEditMapper;

    public Page<OrderReadDto> findAll(OrderFilter filter, Pageable pageable) {
            var predicate = QPredicates.builder()
                    .add(filter.type(), order.type::eq)
                    .add(filter.status(), order.status::eq)
                    .add(filter.user(), order.user.email::containsIgnoreCase)
                    .add(filter.book(), order.book.title::containsIgnoreCase)
                    .build();

        return orderRepository.findAll(predicate, pageable)
                .map(orderReadMapper::map);
    }

    public List<OrderReadDto> findAllByUserId(Long userId) {
        return orderRepository.findAllByUserId(userId).stream()
                .map(orderReadMapper::map)
                .toList();
    }


    public Optional<OrderReadDto> findById(Long id) {
        return orderRepository.findById(id)
                .map(orderReadMapper::map);
    }

    public List<OrderReadDto> findAll() {
        return orderRepository.findAll().stream()
                .map(orderReadMapper::map)
                .toList();
    }

    @Transactional
    public OrderReadDto create(OrderCreateDto orderDto) {
        OrderReadDto orderReadDto = Optional.of(orderDto)
                .map(orderCreateEditMapper::map)
                .map(orderRepository::save)
                .map(orderReadMapper::map)
                .orElseThrow();

        Optional<Book> book = bookRepository.findById(orderDto.getBookId());
        book.ifPresent(value -> {
            value.setNumber(value.getNumber() - 1);
            bookRepository.save(value);
        });
        return orderReadDto;
    }

    @Transactional
    public Optional<OrderReadDto> update(Long id, OrderCreateDto orderDto) {
        Optional<OrderReadDto> orderReadDto = orderRepository.findById(id)
                .map(entity -> orderEditMapper.map(orderDto, entity))
                .map(orderRepository::saveAndFlush)
                .map(orderReadMapper::map);

        if (orderReadDto.isPresent()) {
            Long bookId = orderReadDto.get().getBook().getId();
            Optional<Book> book = bookRepository.findById(bookId);
            book.ifPresent(value -> {
                value.setNumber(value.getNumber() + 1);
                bookRepository.save(value);
            });
        }
        return orderReadDto;
    }

    @Transactional
    public boolean delete(Long id) {
        Boolean result = orderRepository.findById(id)
                .map(entity -> {
                    orderRepository.delete(entity);
                    orderRepository.flush();
                    return true;
                })
                .orElse(false);

//        Optional<Order> order = orderRepository.findById(id);
//        if (order.isPresent() && order.get().getStatus()== OrderStatus.RETURNED) {
//            Book book = order.get().getBook();
//            book.setNumber(book.getNumber() + 1);
//            bookRepository.save(book);
//        }
        return result;
    }
}
