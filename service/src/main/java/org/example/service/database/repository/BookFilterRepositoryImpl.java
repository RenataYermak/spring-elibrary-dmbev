package org.example.service.database.repository;

import com.querydsl.jpa.impl.JPAQuery;
import lombok.AllArgsConstructor;
import org.example.service.database.entity.Book;
import org.example.service.database.querydsl.QPredicate;
import org.example.service.dto.BookFilter;

import javax.persistence.EntityManager;
import java.util.List;

import static org.example.service.database.entity.QAuthor.author;
import static org.example.service.database.entity.QBook.book;
import static org.example.service.database.entity.QCategory.category;

@AllArgsConstructor
public class BookFilterRepositoryImpl implements BookFilterRepository {

    private final EntityManager entityManager;

    @Override
    public List<Book> findByFilter(BookFilter filter) {
        var predicate = QPredicate.builder()
                .add(filter.publishYear(), book.publishYear::eq)
                .add(filter.category(), book.category.name::eq)
                .add(filter.author(), book.author.name::eq)
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
