package org.example.service.mapper.bookMapper;

import org.example.service.database.entity.Author;
import org.example.service.dto.bookDto.AuthorReadDto;
import org.example.service.mapper.Mapper;
import org.springframework.stereotype.Component;

@Component
public class AuthorReadMapper implements Mapper<Author, AuthorReadDto> {

    @Override
    public AuthorReadDto map(Author object) {
        return new AuthorReadDto(
                object.getId(),
                object.getName()
        );
    }
}
