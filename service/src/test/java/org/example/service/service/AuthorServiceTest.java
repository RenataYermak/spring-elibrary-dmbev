package org.example.service.service;

import org.example.service.database.entity.Author;
import org.example.service.database.repository.AuthorRepository;
import org.example.service.dto.bookDto.AuthorReadDto;
import org.example.service.mapper.bookMapper.AuthorReadMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.example.service.util.ConstantUtil.ALL_2_AUTHORS;
import static org.example.service.util.ConstantUtil.AUTHOR_ID_ONE;
import static org.example.service.util.ConstantUtil.AUTHOR_ID_TWO;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
public class AuthorServiceTest {

    @Mock
    private AuthorRepository authorRepository;
    @Mock
    private AuthorReadMapper authorReadMapper;
    @InjectMocks
    private AuthorService authorService;

    @Test
    void findAll() {
        var authors = List.of(getAuthor(), getAnotherAuthor());
        var expectedResult = List.of(getAuthorReadDto(), getAnotherAuthorReadDto());
        doReturn(authors).when(authorRepository).findAll();
        doReturn(getAuthorReadDto(), getAnotherAuthorReadDto()).when(authorReadMapper).map(any(Author.class));

        var actualResult = authorService.findAll();

        assertThat(actualResult).hasSize(ALL_2_AUTHORS);
        assertThat(expectedResult).isEqualTo(actualResult);
    }

    private Author getAuthor() {
        return Author.builder()
                .name("Ernest Hemingway")
                .build();
    }

    private Author getAnotherAuthor() {
        return Author.builder()
                .name("Stephan King")
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
