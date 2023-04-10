package org.example.service.database.repository;

import org.example.service.database.entity.Order;
import org.example.service.dto.orderDto.OrderFilter;

import java.util.List;

public interface OrderFilterRepository {

    List<Order> findAllByFilter(OrderFilter filter);
}
