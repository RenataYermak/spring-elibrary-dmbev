package org.example.service.database.repository;

import org.example.service.database.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;
import java.util.Optional;


public interface OrderRepository extends JpaRepository<Order, Long>, OrderFilterRepository, QuerydslPredicateExecutor<Order> {

    List<Order> findAllByUserId(Long userId);

    @Query(value = "SELECT id " +
            "FROM orders o " +
            "WHERE o.book_id = :bookId AND o.user_id = :userId AND o.status = 'ORDERED' ",
            nativeQuery = true)
    Optional<Long> findByUserIdAndBookId(Long userId, Long bookId);
}
