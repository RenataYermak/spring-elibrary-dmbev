package org.example.service.service;

import lombok.RequiredArgsConstructor;
import org.example.service.database.querydsl.QPredicates;
import org.example.service.database.repository.OrderRepository;
import org.example.service.dto.orderDto.OrderFilter;
import org.example.service.dto.orderDto.OrderReadDto;
import org.example.service.mapper.orderMapper.OrderCreateEditMapper;
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

    private final OrderRepository orderRepository;
    private final OrderReadMapper orderReadMapper;
    private final OrderCreateEditMapper orderCreateEditMapper;

    public Page<OrderReadDto> findAll(OrderFilter filter, Pageable pageable) {
        var predicate = QPredicates.builder()
                .add(filter.type(), order.type::eq)
                .add(filter.status(), order.status::eq)
                .add(filter.user(), order.user.email::eq)
                .add(filter.book(), order.book.title::eq)
                .add(filter.orderedDate(), order.orderedDate::eq)
                .build();

        return orderRepository.findAll(predicate, pageable)
                .map(orderReadMapper::map);
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
    public boolean delete(Long id) {
        return orderRepository.findById(id)
                .map(entity -> {
                    orderRepository.delete(entity);
                    orderRepository.flush();
                    return true;
                })
                .orElse(false);
    }
}
