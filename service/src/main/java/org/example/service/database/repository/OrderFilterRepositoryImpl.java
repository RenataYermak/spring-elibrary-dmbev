package org.example.service.database.repository;

import com.querydsl.jpa.impl.JPAQuery;
import lombok.AllArgsConstructor;
import org.example.service.database.entity.Order;
import org.example.service.database.querydsl.QPredicates;
import org.example.service.dto.orderDto.OrderFilter;

import javax.persistence.EntityManager;
import java.util.List;

import static org.example.service.database.entity.QBook.book;
import static org.example.service.database.entity.QOrder.order;
import static org.example.service.database.entity.QUser.user;

@AllArgsConstructor
public class OrderFilterRepositoryImpl implements OrderFilterRepository {

    private final EntityManager entityManager;

    @Override
    public List<Order> findAllByFilter(OrderFilter filter) {
        var predicate = QPredicates.builder()
                .add(filter.type(), order.type::eq)
                .add(filter.status(), order.status::eq)
                .add(filter.user(), order.user.email::containsIgnoreCase)
                .add(filter.book(), order.book.title::containsIgnoreCase)
                .build();

        return new JPAQuery<Order>(entityManager)
                .select(order)
                .from(order)
                .join(order.user, user)
                .join(order.book, book)
                .where(predicate)
                .fetch();
    }
}
