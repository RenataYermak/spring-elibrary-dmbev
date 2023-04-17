package org.example.service.mapper.bookMapper;

import lombok.RequiredArgsConstructor;
import org.example.service.database.entity.Author;
import org.example.service.database.entity.Book;
import org.example.service.database.entity.Category;
import org.example.service.database.repository.AuthorRepository;
import org.example.service.database.repository.CategoryRepository;
import org.example.service.dto.bookDto.BookCreateEditDto;
import org.example.service.mapper.Mapper;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

import static java.util.function.Predicate.not;


@Component
@RequiredArgsConstructor
public class BookCreateEditMapper implements Mapper<BookCreateEditDto, Book> {

    private final AuthorRepository authorRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public Book map(BookCreateEditDto fromObject, Book toObject) {
        copy(fromObject, toObject);
        return toObject;
    }

    @Override
    public Book map(BookCreateEditDto object) {
        Book book = new Book();
        copy(object, book);
        return book;
    }

    private void copy(BookCreateEditDto object, Book book) {
        book.setTitle(object.getTitle());
        book.setCategory(getCategory(object.getCategoryId()));
        book.setPublishYear(object.getPublishYear());
        book.setDescription(object.getDescription());
        book.setNumber(object.getNumber());
        book.setAuthor(getAuthor(object.getAuthorId()));

        Optional.ofNullable(object.getPicture())
                .filter(not(MultipartFile::isEmpty))
                .ifPresent(picture -> book.setPicture(picture.getOriginalFilename()));
    }

    public Author getAuthor(Long authorId) {
        return Optional.ofNullable(authorId)
                .flatMap(authorRepository::findById)
                .orElse(null);
    }

    public Category getCategory(Integer categoryId) {
        return Optional.ofNullable(categoryId)
                .flatMap(categoryRepository::findById)
                .orElse(null);
    }
}
