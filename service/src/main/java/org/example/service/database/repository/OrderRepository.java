package org.example.service.database.repository;

import org.example.service.database.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;


public interface OrderRepository extends JpaRepository<Order, Long>, OrderFilterRepository, QuerydslPredicateExecutor<Order> {

//    @Query("INSERT INTO orders (book, user, status, type) " +
//            "SELECT b, u, os, ot " +
//            "FROM Book b, User u, OrderStatus os, OrderType ot " +
//            "WHERE b.id = ?1 AND u.id = ?2 " +
//            "AND os.orderStatusName = 'ORDERED' " +
//            "AND ot.orderTypeName = ?3")
//    void createOrder(Long bookId, Long userId, String orderType);
}
