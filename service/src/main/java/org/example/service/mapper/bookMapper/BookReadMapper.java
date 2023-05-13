package org.example.service.mapper.bookMapper;

import lombok.RequiredArgsConstructor;
import org.example.service.database.entity.Book;
import org.example.service.dto.bookDto.BookReadDto;
import org.example.service.mapper.Mapper;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class BookReadMapper implements Mapper<Book, BookReadDto> {

    private final AuthorReadMapper authorReadMapper;
    private final CategoryReadMapper categoryReadMapper;

    @Override
    public BookReadDto map(Book object) {
        var author = Optional.ofNullable(object.getAuthor())
                .map(authorReadMapper::map)
                .orElse(null);
        var category = Optional.ofNullable(object.getCategory())
                .map(categoryReadMapper::map)
                .orElse(null);

        return new BookReadDto(
                object.getId(),
                object.getTitle(),
                category,
                object.getPublishYear(),
                object.getDescription(),
                object.getNumber(),
                author,
                object.getPicture()
        );
    }
}
