package org.example.service.database.repository;

import org.example.service.database.entity.Book;
import org.example.service.dto.bookDto.BookFilter;

import java.util.List;

public interface BookFilterRepository {

    List<Book> findAllByFilter(BookFilter filter);
}
