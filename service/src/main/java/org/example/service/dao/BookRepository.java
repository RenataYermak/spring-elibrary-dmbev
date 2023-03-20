package org.example.service.dao;

import com.querydsl.jpa.impl.JPAQuery;
import org.example.service.database.entity.Book;
import org.example.service.dto.BookFilter;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

import static org.example.service.database.entity.QAuthor.author;
import static org.example.service.database.entity.QBook.book;
import static org.example.service.database.entity.QCategory.category;


@Repository
public class BookRepository extends BaseRepository<Long, Book> {

    public BookRepository(EntityManager entityManager) {
        super(Book.class, entityManager);
    }

    public List<Book> findByFilterQueryDsl(BookFilter filter) {
        var predicate = QPredicate.builder()
                .add(filter.getPublishYear(), book.publishYear::eq)
                .add(filter.getCategory(), book.category.name::eq)
                .add(filter.getAuthor(), book.author.name::eq)
                .buildAnd();

        return new JPAQuery<Book>(entityManager)
                .select(book)
                .from(book)
                .join(book.author, author)
                .join(book.category, category)
                .where(predicate)
                .fetch();
    }
}
