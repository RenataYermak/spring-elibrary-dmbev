package org.example.service.repository;

import lombok.RequiredArgsConstructor;
import org.example.service.database.repository.BookRepository;
import org.example.service.dto.bookDto.BookFilter;
import org.example.service.integration.IntegrationTestBase;
import org.example.service.util.EntityTestUtil;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.example.service.util.ConstantUtil.ALL_BOOKS;
import static org.example.service.util.ConstantUtil.BOOK_ID_ONE;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@RequiredArgsConstructor
class BookRepositoryIT extends IntegrationTestBase {

    private final BookRepository bookRepository;
    private final EntityManager entityManager;

    @Test
    void findById() {
        var actualBook = bookRepository.findById(BOOK_ID_ONE);
        entityManager.clear();

        assertThat(actualBook).isPresent();
        assertThat(actualBook.get().getTitle()).isEqualTo("Death on the Nile");
    }

    @Test
    void findAll() {
        var books = bookRepository.findAll();
        entityManager.clear();

        assertNotNull(books);
        assertThat(books).hasSize(ALL_BOOKS);
    }

    @Test
    void save() {
        var category = EntityTestUtil.getCategory();
        var author = EntityTestUtil.getAuthor();
        var book = EntityTestUtil.getBook(category, author);

        entityManager.persist(category);
        entityManager.persist(author);

        var actualBook = bookRepository.save(book);

        assertThat(actualBook.getId()).isNotNull();
    }

    @Test
    void delete() {
        var category = EntityTestUtil.getCategory();
        var author = EntityTestUtil.getAuthor();
        var book = EntityTestUtil.getBook(category, author);

        entityManager.persist(category);
        entityManager.persist(author);
        entityManager.persist(book);

        bookRepository.delete(book);
        entityManager.flush();
        entityManager.clear();

        var deletedBook = bookRepository.findById(book.getId());

        assertThat(deletedBook).isEmpty();
    }

    @Test
    void update() {
        var expectedBook = bookRepository.findById(BOOK_ID_ONE).get();
        expectedBook.setTitle("New Title");
        bookRepository.saveAndFlush(expectedBook);
        entityManager.clear();

        var actualBook = bookRepository.findById(BOOK_ID_ONE);

        assertThat(actualBook).isPresent();
        assertThat(actualBook.get().getTitle()).isEqualTo("New Title");
    }

    @Test
    void findByFilterQueryDslWithAllParameters() {
        var filter = BookFilter.builder()
                .publishYear(1937)
                .category("Detective")
                .author("Agatha Christie")
                .build();

        var books = bookRepository.findAllByFilter(filter);

        assertNotNull(books);
        assertThat(books).hasSize(1);
        assertThat(books.get(0).getPublishYear()).isEqualTo(1937);
        assertThat(books.get(0).getCategory().getName()).isEqualTo("Detective");
        assertThat(books.get(0).getAuthor().getName()).isEqualTo("Agatha Christie");
    }

    @Test
    void findByFilterQueryDslWithTwoParameters() {
        var filter = BookFilter.builder()
                .publishYear(1937)
                .category("Detective")
                .build();

        var books = bookRepository.findAllByFilter(filter);

        assertNotNull(books);
        assertThat(books).hasSize(2);
        assertThat(books.get(0).getPublishYear()).isEqualTo(1937);
        assertThat(books.get(0).getCategory().getName()).isEqualTo("Detective");
        assertThat(books.get(1).getPublishYear()).isEqualTo(1937);
        assertThat(books.get(1).getCategory().getName()).isEqualTo("Detective");
    }

    @Test
    void findByFilterQueryDslWithoutParameters() {
        var filter = BookFilter.builder()
                .build();

        var books = bookRepository.findAllByFilter(filter);

        assertThat(books).hasSize(bookRepository.findAll().size());
    }
}
