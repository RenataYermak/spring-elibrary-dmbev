package org.example.service.service;

import com.querydsl.core.types.Predicate;
import org.example.service.database.entity.Author;
import org.example.service.database.entity.Book;
import org.example.service.database.entity.Category;
import org.example.service.database.entity.Order;
import org.example.service.database.entity.OrderStatus;
import org.example.service.database.entity.OrderType;
import org.example.service.database.entity.Role;
import org.example.service.database.querydsl.QPredicates;
import org.example.service.database.repository.BookRepository;
import org.example.service.database.repository.OrderRepository;
import org.example.service.dto.bookDto.AuthorReadDto;
import org.example.service.dto.bookDto.BookReadDto;
import org.example.service.dto.bookDto.CategoryReadDto;
import org.example.service.dto.orderDto.OrderCreateEditDto;
import org.example.service.dto.orderDto.OrderFilter;
import org.example.service.dto.orderDto.OrderReadDto;
import org.example.service.dto.userDto.UserReadDto;
import org.example.service.mapper.orderMapper.OrderCreateMapper;
import org.example.service.mapper.orderMapper.OrderEditMapper;
import org.example.service.mapper.orderMapper.OrderReadMapper;
import org.example.service.util.EntityTestUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.example.service.database.entity.QOrder.order;
import static org.example.service.util.ConstantUtil.ALL_2_ORDERS;
import static org.example.service.util.ConstantUtil.AUTHOR_ID_ONE;
import static org.example.service.util.ConstantUtil.BOOK_ID_ONE;
import static org.example.service.util.ConstantUtil.CATEGORY_ID_ONE;
import static org.example.service.util.ConstantUtil.ORDER_ID_ONE;
import static org.example.service.util.ConstantUtil.ORDER_ID_TWO;
import static org.example.service.util.ConstantUtil.PAGE;
import static org.example.service.util.ConstantUtil.SIZE;
import static org.example.service.util.ConstantUtil.USER_ID_ONE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderReadMapper orderReadMapper;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private OrderCreateMapper orderCreateEditMapper;

    @Mock
    private OrderEditMapper orderEditMapper;

    @InjectMocks
    private OrderService orderService;

    @Test
    void findAll() {
        var orders = List.of(getOrder(), getAnotherOrder());
        var expectedResult = List.of(getOrderReadDto(), getAnotherOrderReadDto());
        doReturn(orders).when(orderRepository).findAll();
        doReturn(getOrderReadDto(), getAnotherOrderReadDto()).when(orderReadMapper).map(any(Order.class));

        var actualResult = orderService.findAll();

        assertThat(actualResult).hasSize(ALL_2_ORDERS);
        assertThat(expectedResult).isEqualTo(actualResult);
    }

    @Test
    void findAllWithFilter() {
        var pageable = PageRequest.of(PAGE, SIZE);
        var filter = new OrderFilter(OrderType.READING_ROOM, OrderStatus.ORDERED, "renata@gmail.com", "title");
        var predicate = getPredicate(filter);
        var orders = new PageImpl<>(List.of(getOrder(), getAnotherOrder()), pageable, SIZE);
        var expectedResult = new PageImpl<>(List.of(
                getOrderReadDto(), getAnotherOrderReadDto()), pageable, SIZE);
        doReturn(orders).when(orderRepository).findAll(predicate, pageable);
        doReturn(getOrderReadDto(), getAnotherOrderReadDto()).when(orderReadMapper).map(any(Order.class));

        var actualResult = orderService.findAll(filter, pageable);

        assertThat(actualResult).hasSize(2);
        assertThat(expectedResult).isEqualTo(actualResult);
    }

    @Test
    void findById() {
        var order = getOrder();
        var expectedResult = getOrderReadDto();
        when((orderRepository).findById(ORDER_ID_ONE)).thenReturn(Optional.of(order));
        doReturn(expectedResult).when(orderReadMapper).map(order);

        var actualResult = orderService.findById(ORDER_ID_ONE);

        assertTrue(actualResult.isPresent());
        actualResult.ifPresent(actual -> assertEquals(expectedResult, actual));
    }

    @Test
    void createSuccessful() {
        var orderCreateEditDto = getOrderCreateDto();
        var order = getOrder();
        var book = getBook(getCategory(), getAuthor());
        var expectedResult = getOrderReadDto();
        when(orderRepository.findByUserIdAndBookId(orderCreateEditDto.getUserId(), orderCreateEditDto.getBookId())).thenReturn(Optional.empty());
        doReturn(order).when(orderCreateEditMapper).map(orderCreateEditDto);
        doReturn(order).when(orderRepository).save(order);
        doReturn(book).when(bookRepository).findById(BOOK_ID_ONE);
        doReturn(expectedResult).when(orderReadMapper).map(order);

        var actualResult = orderService.create(orderCreateEditDto);

        assertThat(actualResult.getId()).isNotNull();
        assertThat(expectedResult).isEqualTo(actualResult);
    }

    @Test
    void createOrderThatAlreadyExistsThrowsIllegalArgumentException() {
        var orderDto = getOrderCreateDto();

        when(orderRepository.findByUserIdAndBookId(orderDto.getUserId(), orderDto.getBookId())).thenReturn(Optional.of(ORDER_ID_TWO));

        assertThrows(IllegalArgumentException.class, () -> orderService.create(orderDto));

        verify(orderRepository).findByUserIdAndBookId(orderDto.getUserId(), orderDto.getBookId());
        verifyNoInteractions(orderCreateEditMapper, orderReadMapper);
    }

    @Test
    void update() {
        var order = getOrder();
        var orderCreateEditDto = getUpdatedOrderDto();
        var updatedOrder = getUpdatedOrder();
        var book = getBook(getCategory(), getAuthor());
        var expectedResult = getUpdatedOrderReadDto();
        doReturn(Optional.of(order)).when(orderRepository).findById(ORDER_ID_ONE);
        doReturn(updatedOrder).when(orderEditMapper).map(orderCreateEditDto, order);
        doReturn(updatedOrder).when(orderRepository).saveAndFlush(updatedOrder);
        doReturn(book).when(bookRepository).findById(BOOK_ID_ONE);
        doReturn(expectedResult).when(orderReadMapper).map(updatedOrder);

        var actualResult = orderService.returnBook(ORDER_ID_ONE, orderCreateEditDto);

        assertThat(Optional.of(expectedResult)).isEqualTo(actualResult);
    }

    @Test
    void delete() {
        var order = getOrder();
        when((orderRepository).findById(ORDER_ID_ONE)).thenReturn(Optional.of(order));

        var expectedResult = orderService.delete(USER_ID_ONE);

        assertTrue(expectedResult);
    }

    private Order getOrder() {
        return Order.builder()
                .id(ORDER_ID_ONE)
                .book(EntityTestUtil.getBook(getCategory(), getAuthor()))
                .user(EntityTestUtil.getUser())
                .status(OrderStatus.ORDERED)
                .type(OrderType.READING_ROOM)
                .orderedDate(LocalDateTime.of(2023, 5, 8, 10, 32, 0, 640000))
                .returnedDate(null)
                .build();
    }

    private Order getAnotherOrder() {
        return Order.builder()
                .id(ORDER_ID_TWO)
                .book(EntityTestUtil.getBook(getCategory(), getAuthor()))
                .user(EntityTestUtil.getUser())
                .status(OrderStatus.RETURNED)
                .type(OrderType.SEASON_TICKET)
                .orderedDate(LocalDateTime.of(2023, 5, 8, 10, 32, 0, 640000))
                .returnedDate(LocalDateTime.of(2023, 5, 9, 10, 32, 0, 640000))
                .build();
    }

    private OrderReadDto getOrderReadDto() {
        return new OrderReadDto(
                ORDER_ID_ONE,
                getUserReadDto(),
                getBookReadDto(),
                OrderStatus.ORDERED,
                OrderType.READING_ROOM,
                LocalDateTime.of(2023, 5, 8, 10, 32, 0, 640000),
                null
        );
    }

    private OrderReadDto getAnotherOrderReadDto() {
        return new OrderReadDto(
                ORDER_ID_TWO,
                getUserReadDto(),
                getBookReadDto(),
                OrderStatus.RETURNED,
                OrderType.SEASON_TICKET,
                LocalDateTime.of(2023, 5, 8, 10, 32, 0, 640000),
                LocalDateTime.of(2023, 5, 9, 10, 32, 0, 640000)
        );
    }

    private OrderCreateEditDto getOrderCreateDto() {
        return new OrderCreateEditDto(
                getUserReadDto().getId(),
                getBookReadDto().getId(),
                OrderStatus.ORDERED,
                OrderType.READING_ROOM,
                LocalDateTime.of(2023, 5, 8, 10, 32, 0, 640000),
                null
        );
    }

    private OrderCreateEditDto getUpdatedOrderDto() {
        return new OrderCreateEditDto(
                getUserReadDto().getId(),
                getBookReadDto().getId(),
                OrderStatus.RETURNED,
                OrderType.SEASON_TICKET,
                LocalDateTime.of(2023, 5, 8, 10, 32, 0, 640000),
                LocalDateTime.of(2023, 5, 9, 10, 32, 0, 640000)
        );
    }

    private Order getUpdatedOrder() {
        return Order.builder()
                .id(ORDER_ID_ONE)
                .book(EntityTestUtil.getBook(getCategory(), getAuthor()))
                .user(EntityTestUtil.getUser())
                .status(OrderStatus.RETURNED)
                .type(OrderType.READING_ROOM)
                .orderedDate(LocalDateTime.of(2023, 5, 8, 10, 32, 0, 640000))
                .returnedDate(LocalDateTime.of(2023, 5, 9, 10, 32, 0, 640000))
                .build();
    }

    private OrderReadDto getUpdatedOrderReadDto() {
        return new OrderReadDto(
                BOOK_ID_ONE,
                getUserReadDto(),
                getBookReadDto(),
                OrderStatus.ORDERED,
                OrderType.SEASON_TICKET,
                LocalDateTime.of(2023, 5, 8, 10, 32, 0, 640000),
                null
        );
    }

    private Predicate getPredicate(OrderFilter filter) {
        return QPredicates.builder()
                .add(filter.type(), order.type::eq)
                .add(filter.status(), order.status::eq)
                .add(filter.user(), order.user.email::containsIgnoreCase)
                .add(filter.book(), order.book.title::containsIgnoreCase)
                .build();
    }

    private Category getCategory() {
        return Category.builder()
                .name("Novel")
                .build();
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

    private Author getAuthor() {
        return Author.builder()
                .name("Ernest Hemingway")
                .build();
    }

    private AuthorReadDto getAuthorReadDto() {
        return new AuthorReadDto(
                AUTHOR_ID_ONE,
                "Ernest Hemingway"
        );
    }

    private Optional<Book> getBook(Category category, Author author) {
        return Optional.ofNullable(Book.builder()
                .title("title")
                .author(author)
                .publishYear(2000)
                .category(category)
                .description("text")
                .number(12)
                .picture("picture")
                .build());
    }
}