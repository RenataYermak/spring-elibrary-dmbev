package org.example.service.http.controller.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.example.service.dto.bookDto.AuthorReadDto;
import org.example.service.dto.bookDto.BookCreateEditDto;
import org.example.service.dto.bookDto.BookReadDto;
import org.example.service.dto.bookDto.CategoryReadDto;
import org.example.service.integration.IntegrationTestBase;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
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


//    @Test
//    void create() throws Exception {
//        var bookCreateDto = getBookCreateDto();
//        mockMvc.perform(MockMvcRequestBuilders.post(URL_REST_PREFIX)
//                        .with(csrf())
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(bookCreateDto))
//                )
//                .andExpectAll(
//                        status().isCreated(),
//                        content().contentType(MediaType.APPLICATION_JSON),
//                        jsonPath("$.title").exists(),
//                        jsonPath("$.category").exists(),
//                        jsonPath("$.author").exists(),
//                        jsonPath("$.publishYear").exists(),
//                        jsonPath("$.description").exists(),
//                        jsonPath("$.number").exists(),
//                        jsonPath("$.picture").exists()
//                );
//    }
//
//    @Test
//    void update() throws Exception {
//        var bookReadDto = getBookReadDto();
//        var updatedBookDto = getUpdatedBookDto();
//        var bookId = bookReadDto.getId();
//        mockMvc.perform(put(URL_REST_PREFIX + "/" + bookId + "/update")
//                        .with(csrf())
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(updatedBookDto))
//                )
//                .andExpectAll(
//                        status().is2xxSuccessful(),
//                        content().contentType(MediaType.APPLICATION_JSON),
//                        jsonPath("$.title").exists(),
//                        jsonPath("$.category").exists(),
//                        jsonPath("$.author").exists(),
//                        jsonPath("$.publishYear").exists(),
//                        jsonPath("$.description").exists(),
//                        jsonPath("$.number").exists(),
//                        jsonPath("$.picture").exists()
//                );
//    }

    @Test
    void delete() throws Exception {
        var bookReadDto = getBookReadDto();
        Long id = bookReadDto.getId();
        mockMvc.perform(MockMvcRequestBuilders.delete(URL_REST_PREFIX + "/" + id + "/delete")
                        .with(csrf()))
                .andExpectAll(
                        status().isNoContent()
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

    private BookCreateEditDto getBookCreateDto() {
        return new BookCreateEditDto(
                "title",
                getAuthorReadDto().getId(),
                getCategoryReadDto().getId(),
                2000,
                "description",
                12,
                new MockMultipartFile("test", new byte[0])
        );
    }

    private BookCreateEditDto getUpdatedBookDto() {
        return new BookCreateEditDto(
                "titleNew",
                getAuthorReadDto().getId(),
                getCategoryReadDto().getId(),
                2021,
                "description",
                12,
                new MockMultipartFile("test", new byte[0])
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