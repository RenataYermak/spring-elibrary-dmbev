package org.example.service.service;

import lombok.RequiredArgsConstructor;
import org.example.service.database.entity.OrderStatus;
import org.example.service.database.entity.OrderType;
import org.example.service.database.entity.Role;
import org.example.service.dto.bookDto.AuthorReadDto;
import org.example.service.dto.bookDto.BookReadDto;
import org.example.service.dto.bookDto.CategoryReadDto;
import org.example.service.dto.orderDto.OrderCreateEditDto;
import org.example.service.dto.orderDto.OrderReadDto;
import org.example.service.dto.userDto.UserReadDto;
import org.example.service.integration.IntegrationTestBase;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.example.service.util.ConstantUtil.ALL_ORDERS;
import static org.example.service.util.ConstantUtil.AUTHOR_ID_ONE;
import static org.example.service.util.ConstantUtil.BOOK_ID_FOUR;
import static org.example.service.util.ConstantUtil.BOOK_ID_ONE;
import static org.example.service.util.ConstantUtil.CATEGORY_ID_ONE;
import static org.example.service.util.ConstantUtil.ORDER_ID_ONE;
import static org.example.service.util.ConstantUtil.ORDER_ID_TWO;
import static org.example.service.util.ConstantUtil.USER_ID_ONE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

@RequiredArgsConstructor
public class OrderServiceIT extends IntegrationTestBase {

    private final OrderService orderService;

    @Test
    void findAll() {
        List<OrderReadDto> result = orderService.findAll();
        assertThat(result).hasSize(ALL_ORDERS);
    }

    @Test
    void findById() {
        Optional<OrderReadDto> maybeOrder = orderService.findById(ORDER_ID_ONE);
        assertTrue(maybeOrder.isPresent());
        maybeOrder.ifPresent(order -> {
            assertEquals("renatayermak@gmail.com", order.getUser().getEmail());
            assertEquals("Death on the Nile", order.getBook().getTitle());
        });
    }

    @Test
    void create() {
        OrderCreateEditDto orderDto = new OrderCreateEditDto(
                getUserReadDto().getId(),
                getBookReadDto().getId(),
                OrderStatus.ORDERED,
                OrderType.READING_ROOM,
                LocalDateTime.now(),
                null
        );
        OrderReadDto actualResult = orderService.create(orderDto);
        assertEquals(orderDto.getUserId(), actualResult.getUser().getId());
        assertEquals(orderDto.getBookId(), actualResult.getBook().getId());
        assertSame(orderDto.getStatus(), actualResult.getStatus());
        assertSame(orderDto.getType(), actualResult.getType());
    }

    @Test
    void update() {
        OrderCreateEditDto orderDto = new OrderCreateEditDto(
                USER_ID_ONE,
                BOOK_ID_FOUR,
                OrderStatus.RETURNED,
                OrderType.READING_ROOM,
                LocalDateTime.of(2023, 3, 4, 6, 54, 0, 000000),
                LocalDateTime.now()
        );
        Optional<OrderReadDto> actualResult = orderService.returnBook(ORDER_ID_TWO, orderDto);
        assertTrue(actualResult.isPresent());

        actualResult.ifPresent(
                order -> {
                    assertEquals(orderDto.getUserId(), actualResult.get().getUser().getId());
                    assertEquals(orderDto.getBookId(), actualResult.get().getBook().getId());
                    assertSame(orderDto.getStatus(), actualResult.get().getStatus());
                    assertSame(orderDto.getType(), actualResult.get().getType());
                    assertEquals(orderDto.getOrderedDate(), actualResult.get().getOrdered_date());
                }
        );
    }

   @Test
    void delete() {
        assertFalse(orderService.delete(-2345678L));
        assertTrue(orderService.delete(ORDER_ID_ONE));
    }

    private CategoryReadDto getCategoryReadDto() {
        return new CategoryReadDto(
                CATEGORY_ID_ONE,
                "Novel"
        );
    }

    private UserReadDto getUserReadDto() {
        return new UserReadDto(
                USER_ID_ONE,
                "Renata",
                "Yermak",
                "renata@gmail.com",
                Role.ADMIN
        );
    }

    private BookReadDto getBookReadDto() {
        return new BookReadDto(
                BOOK_ID_ONE,
                "title",
                getCategoryReadDto(),
                2000,
                "description",
                12,
                getAuthorReadDto(),
                "1.png"

        );
    }

    private AuthorReadDto getAuthorReadDto() {
        return new AuthorReadDto(
                AUTHOR_ID_ONE,
                "Ernest Hemingway"
        );
    }
}