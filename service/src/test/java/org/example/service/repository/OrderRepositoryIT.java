package org.example.service.repository;

import lombok.RequiredArgsConstructor;
import org.example.service.database.entity.OrderStatus;
import org.example.service.database.entity.OrderType;
import org.example.service.database.repository.OrderRepository;
import org.example.service.dto.orderDto.OrderFilter;
import org.example.service.integration.IntegrationTestBase;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.example.service.util.ConstantUtil.ALL_ORDERS;
import static org.example.service.util.ConstantUtil.ORDER_ID_ONE;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@RequiredArgsConstructor
public class OrderRepositoryIT extends IntegrationTestBase {

    private final OrderRepository orderRepository;
    private final EntityManager entityManager;

    @Test
    void findById() {
        var actualOrder = orderRepository.findById(ORDER_ID_ONE);
        entityManager.clear();

        assertThat(actualOrder).isPresent();
        assertThat(actualOrder.get().getType()).isEqualTo(OrderType.SEASON_TICKET);
    }

    @Test
    void findAll() {
        var orders = orderRepository.findAll();
        entityManager.clear();

        assertNotNull(orders);
        assertThat(orders).hasSize(ALL_ORDERS);
    }

    @Test
    void save() {
        var category = EntityTestUtil.getCategory();
        var author = EntityTestUtil.getAuthor();
        var book = EntityTestUtil.getBook(category, author);
        var user = EntityTestUtil.getUser();
        var order = EntityTestUtil.getOrder(book, user);

        entityManager.persist(category);
        entityManager.persist(author);
        entityManager.persist(book);
        entityManager.persist(user);

        var actualOrder = orderRepository.save(order);

        assertThat(actualOrder.getId()).isNotNull();
    }

    @Test
    void delete() {
        var category = EntityTestUtil.getCategory();
        var author = EntityTestUtil.getAuthor();
        var book = EntityTestUtil.getBook(category, author);
        var user = EntityTestUtil.getUser();
        var order = EntityTestUtil.getOrder(book, user);

        entityManager.persist(category);
        entityManager.persist(author);
        entityManager.persist(book);
        entityManager.persist(user);

        orderRepository.save(order);
        orderRepository.delete(order);
        entityManager.flush();
        entityManager.clear();

        var deletedOrder = orderRepository.findById(order.getId());

        assertThat(deletedOrder).isEmpty();
    }

    @Test
    void update() {
        var expectedOrder = orderRepository.findById(ORDER_ID_ONE).get();
        expectedOrder.setType(OrderType.READING_ROOM);
        orderRepository.saveAndFlush(expectedOrder);
        entityManager.clear();

        var actualOrder = orderRepository.findById(ORDER_ID_ONE);

        assertThat(actualOrder).isPresent();
        assertThat(actualOrder.get().getType()).isEqualTo(OrderType.READING_ROOM);
    }

    @Test
    void findByFilterQueryDslWithAllParameters() {
        var filter = OrderFilter.builder()
                .type(OrderType.READING_ROOM)
                .status(OrderStatus.ORDERED)
                .user("eva@gmail.com")
                .book("The Premature Burial")
                .orderedDate(LocalDateTime.of(2018, 4, 22, 5, 24))
                .build();

        var orders = orderRepository.findAllByFilter(filter);

        assertNotNull(orders);
        assertThat(orders).hasSize(1);
        assertThat(orders.get(0).getType()).isEqualTo(OrderType.READING_ROOM);
        assertThat(orders.get(0).getStatus()).isEqualTo(OrderStatus.ORDERED);
        assertThat(orders.get(0).getUser().getEmail()).isEqualTo("eva@gmail.com");
        assertThat(orders.get(0).getBook().getTitle()).isEqualTo("The Premature Burial");
        assertThat(orders.get(0).getOrderedDate()).isEqualTo(LocalDateTime.of(2018, 4, 22, 5, 24));
    }

    @Test
    void findByFilterQueryDslWithTwoParameters() {
        var filter = OrderFilter.builder()
                .type(OrderType.READING_ROOM)
                .status(OrderStatus.ORDERED)
                .build();

        var orders = orderRepository.findAllByFilter(filter);

        assertNotNull(orders);
        assertThat(orders).hasSize(2);
        assertThat(orders.get(0).getType()).isEqualTo(OrderType.READING_ROOM);
        assertThat(orders.get(0).getStatus()).isEqualTo(OrderStatus.ORDERED);
        assertThat(orders.get(1).getType()).isEqualTo(OrderType.READING_ROOM);
        assertThat(orders.get(1).getStatus()).isEqualTo(OrderStatus.ORDERED);
    }

    @Test
    void findByFilterQueryDslWithoutParameters() {
        var filter = OrderFilter.builder()
                .build();

        var orders = orderRepository.findAllByFilter(filter);

        assertThat(orders).hasSize(orderRepository.findAll().size());
    }
}
