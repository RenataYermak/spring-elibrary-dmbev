package org.example.service.database.repository;

import org.example.service.database.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long>, BookFilterRepository {
}
