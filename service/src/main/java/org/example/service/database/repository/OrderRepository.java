package org.example.service.database.repository;

import org.example.service.database.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface OrderRepository extends JpaRepository<Order, Long>, OrderFilterRepository, QuerydslPredicateExecutor<Order> {
}
