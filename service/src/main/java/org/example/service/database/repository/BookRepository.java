package org.example.service.database.repository;

import org.example.service.database.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface BookRepository extends JpaRepository<Book, Long>, BookFilterRepository, QuerydslPredicateExecutor<Book> {
}
