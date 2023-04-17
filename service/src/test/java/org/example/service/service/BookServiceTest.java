package org.example.service.service;

import com.querydsl.core.types.Predicate;
import org.example.service.database.entity.Author;
import org.example.service.database.entity.Book;
import org.example.service.database.entity.Category;
import org.example.service.database.querydsl.QPredicates;
import org.example.service.database.repository.BookRepository;
import org.example.service.dto.bookDto.AuthorReadDto;
import org.example.service.dto.bookDto.BookCreateEditDto;
import org.example.service.dto.bookDto.BookFilter;
import org.example.service.dto.bookDto.BookReadDto;
import org.example.service.dto.bookDto.CategoryReadDto;
import org.example.service.mapper.bookMapper.BookCreateEditMapper;
import org.example.service.mapper.bookMapper.BookReadMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.example.service.database.entity.QBook.book;
import static org.example.service.util.ConstantUtil.ALL_2_BOOKS;
import static org.example.service.util.ConstantUtil.AUTHOR_ID_ONE;
import static org.example.service.util.ConstantUtil.AUTHOR_ID_TWO;
import static org.example.service.util.ConstantUtil.BOOK_ID_ONE;
import static org.example.service.util.ConstantUtil.BOOK_ID_TWO;
import static org.example.service.util.ConstantUtil.CATEGORY_ID_ONE;
import static org.example.service.util.ConstantUtil.CATEGORY_ID_TWO;
import static org.example.service.util.ConstantUtil.PAGE;
import static org.example.service.util.ConstantUtil.SIZE;
import static org.example.service.util.ConstantUtil.USER_ID_ONE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private BookReadMapper bookReadMapper;

    @Mock
    private BookCreateEditMapper bookCreateEditMapper;

    @InjectMocks
    private BookService bookService;

    @Test
    void findAll() {
        var books = List.of(getBook(), getAnotherBook());
        var expectedResult = List.of(getBookReadDto(), getAnotherBookReadDto());
        doReturn(books).when(bookRepository).findAll();
        doReturn(getBookReadDto(), getAnotherBookReadDto()).when(bookReadMapper).map(any(Book.class));

        var actualResult = bookService.findAll();

        assertThat(actualResult).hasSize(ALL_2_BOOKS);
        assertThat(expectedResult).isEqualTo(actualResult);
    }

    @Test
    void findAllWithFilter() {
        var pageable = PageRequest.of(PAGE, SIZE);
        var filter = new BookFilter(1937, "Detective", "Agata");
        var predicate = getPredicate(filter);
        Page<Book> books = new PageImpl<>(List.of(getBook(), getAnotherBook()), pageable, SIZE);
        Page<BookReadDto> expectedResult = new PageImpl<>(List.of(
                getBookReadDto(), getAnotherBookReadDto()), pageable, SIZE);
        doReturn(books).when(bookRepository).findAll(predicate, pageable);
        doReturn(getBookReadDto(), getAnotherBookReadDto()).when(bookReadMapper).map(any(Book.class));

        var actualResult = bookService.findAll(filter, pageable);

        assertThat(actualResult).hasSize(2);
        assertThat(expectedResult).isEqualTo(actualResult);
    }

    @Test
    void findById() {
        var book = getBook();
        var expectedResult = getBookReadDto();
        doReturn(Optional.of(book)).when(bookRepository).findById(USER_ID_ONE);
        doReturn(expectedResult).when(bookReadMapper).map(book);

        var actualResult = bookService.findById(USER_ID_ONE);

        assertTrue(actualResult.isPresent());
        actualResult.ifPresent(actual -> assertEquals(expectedResult, actual));
    }

    @Test
    void create() {
        var bookCreateEditDto = getBookCreateDto();
        var book = getBook();
        var expectedResult = getBookReadDto();
        doReturn(book).when(bookCreateEditMapper).map(bookCreateEditDto);
        doReturn(book).when(bookRepository).save(book);
        doReturn(expectedResult).when(bookReadMapper).map(book);

        var actualResult = bookService.create(bookCreateEditDto);

        assertThat(actualResult.getId()).isNotNull();
        assertThat(expectedResult).isEqualTo(actualResult);
    }

    @Test
    void update() {
        var book = getBook();
        var bookCreateEditDto = getUpdatedBookDto();
        var updatedBook = getUpdatedBook();
        var expectedResult = getUpdatedBookReadDto();
        doReturn(Optional.of(book)).when(bookRepository).findById(USER_ID_ONE);
        doReturn(updatedBook).when(bookCreateEditMapper).map(bookCreateEditDto, book);
        doReturn(updatedBook).when(bookRepository).saveAndFlush(updatedBook);
        doReturn(expectedResult).when(bookReadMapper).map(updatedBook);

        var actualResult = bookService.update(USER_ID_ONE, bookCreateEditDto);

        assertThat(Optional.of(expectedResult)).isEqualTo(actualResult);
    }

    @Test
    void delete() {
        var book = getBook();
        doReturn(Optional.of(book)).when(bookRepository).findById(USER_ID_ONE);

        var expectedResult = bookService.delete(USER_ID_ONE);

        assertTrue(expectedResult);
    }

    private Book getBook() {
        return Book.builder()
                .id(BOOK_ID_ONE)
                .title("title")
                .author(getAuthor())
                .publishYear(2000)
                .category(getCategory())
                .description("description")
                .number(12)
                .picture("1.png")
                .build();
    }

    private Book getAnotherBook() {
        return Book.builder()
                .id(BOOK_ID_TWO)
                .title("titleNew")
                .author(getAuthor())
                .publishYear(2020)
                .category(getCategory())
                .description("text")
                .number(1)
                .picture("3.png")
                .build();
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

    private BookReadDto getAnotherBookReadDto() {
        return new BookReadDto(
                BOOK_ID_TWO,
                "titleNew",
                getAnotherCategoryReadDto(),
                2020,
                "text",
                1,
                getAnotherAuthorReadDto(),
                "3.png"
        );
    }

    private BookCreateEditDto getBookCreateDto() {
        return new BookCreateEditDto(
                "title",
                getAuthorReadDto().getId(),
                getCategoryReadDto().getId(),
                2000,
                "description",
                12,
                "1.png"
        );
    }

    private BookCreateEditDto getUpdatedBookDto() {
        return new BookCreateEditDto(
                "titleNew",
                getAuthorReadDto().getId(),
                getCategoryReadDto().getId(),
                2021,
                "description",
                12,
                "3.png"
        );
    }

    private Book getUpdatedBook() {
        return Book.builder()
                .id(BOOK_ID_ONE)
                .title("titleNew")
                .author(getAuthor())
                .publishYear(2021)
                .category(getCategory())
                .description("description")
                .number(12)
                .picture("3.png")
                .build();
    }

    private BookReadDto getUpdatedBookReadDto() {
        return new BookReadDto(
                BOOK_ID_ONE,
                "titleNew",
                getCategoryReadDto(),
                2021,
                "description",
                12,
                getAuthorReadDto(),
                "3.png"
        );
    }

    private Predicate getPredicate(BookFilter filter) {
        return QPredicates.builder()
                .add(filter.publishYear(), book.publishYear::eq)
                .add(filter.category(), book.category.name::eq)
                .add(filter.author(), book.author.name::eq)
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

    private CategoryReadDto getAnotherCategoryReadDto() {
        return new CategoryReadDto(
                CATEGORY_ID_TWO,
                "Drama"

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

    private AuthorReadDto getAnotherAuthorReadDto() {
        return new AuthorReadDto(
                AUTHOR_ID_TWO,
                "Stephan King"

        );
    }
}