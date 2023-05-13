package org.example.service.http.controller.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.example.service.dto.bookDto.AuthorReadDto;
import org.example.service.dto.bookDto.BookReadDto;
import org.example.service.dto.bookDto.CategoryReadDto;
import org.example.service.integration.IntegrationTestBase;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.example.service.util.ConstantUtil.AUTHOR_ID_ONE;
import static org.example.service.util.ConstantUtil.BOOK_ID_ONE;
import static org.example.service.util.ConstantUtil.CATEGORY_ID_ONE;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@RequiredArgsConstructor
class BookRestControllerTest extends IntegrationTestBase {

    private static final String URL_REST_PREFIX = "/api/v1/books";
    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;

    @Test
    void findAll() throws Exception {
        mockMvc.perform(get(URL_REST_PREFIX))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content").exists())
                .andExpect(jsonPath("$.metadata").exists());
    }

    @Test
    void findById() throws Exception {
        mockMvc.perform(get(URL_REST_PREFIX + "/" + BOOK_ID_ONE))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.title").value("Death on the Nile"));
    }

    @Test
    void deleteSuccessfully() throws Exception {
        var bookReadDto = getBookReadDto();
        Long id = bookReadDto.getId();
        mockMvc.perform(MockMvcRequestBuilders.delete(URL_REST_PREFIX + "/" + id + "/delete")
                        .with(csrf()))
                .andExpectAll(
                        status().isNoContent()
                );
    }

    @Test
    void deleteNotExistBook() throws Exception {
        long id = 999999999999999L;
        mockMvc.perform(MockMvcRequestBuilders.delete(URL_REST_PREFIX + "/" + id + "/delete")
                        .with(csrf()))
                .andExpectAll(
                        status().isNotFound()
                );
    }

    private BookReadDto getBookReadDto() {
        return new BookReadDto(
                BOOK_ID_ONE,
                "title",
                getCategoryReadDto(),
                2000,
                "description",
                12,
                getAuthorReadDto(),
                "1.png"

        );
    }

    private CategoryReadDto getCategoryReadDto() {
        return new CategoryReadDto(
                CATEGORY_ID_ONE,
                "Novel"
        );
    }

    private AuthorReadDto getAuthorReadDto() {
        return new AuthorReadDto(
                AUTHOR_ID_ONE,
                "Ernest Hemingway"
        );
    }
}