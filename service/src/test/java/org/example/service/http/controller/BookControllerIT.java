package org.example.service.http.controller;

import lombok.RequiredArgsConstructor;
import org.example.service.database.entity.Role;
import org.example.service.integration.IntegrationTestBase;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static org.example.service.dto.bookDto.BookCreateEditDto.Fields.authorId;
import static org.example.service.dto.bookDto.BookCreateEditDto.Fields.categoryId;
import static org.example.service.dto.bookDto.BookCreateEditDto.Fields.description;
import static org.example.service.dto.bookDto.BookCreateEditDto.Fields.number;
import static org.example.service.dto.bookDto.BookCreateEditDto.Fields.picture;
import static org.example.service.dto.bookDto.BookCreateEditDto.Fields.publishYear;
import static org.example.service.dto.bookDto.BookCreateEditDto.Fields.title;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@AutoConfigureMockMvc
@RequiredArgsConstructor
public class BookControllerIT extends IntegrationTestBase {

    private final MockMvc mockMvc;

    @Test
    void findAll() throws Exception {
        mockMvc.perform(get("/books"))
                .andExpectAll(
                        status().is2xxSuccessful(),
                        view().name("book/books"),
                        model().attributeExists("books"),
                        model().attributeExists("filter")
                );
    }

    @Test
    void findById() throws Exception {
        mockMvc.perform(get("/books/1"))
                .andExpectAll(
                        status().is2xxSuccessful(),
                        view().name("book/book"),
                        model().attributeExists("book")
                );
    }

    @Test
    void create() throws Exception {
        mockMvc.perform(post("/books")
                        .with(csrf())
                        .param(title, "test")
                        .param(categoryId, "1")
                        .param(publishYear, "2021")
                        .param(description, "description")
                        .param(authorId, "1")
                        .param(number, "2")
                        .param(picture, "1.png")
                )
                .andExpectAll(
                        status().is3xxRedirection(),
                        redirectedUrl("/books/add")
                );
    }

    @Test
    void addBook() throws Exception {
        mockMvc.perform(get("/books/add")
                        .with(user("test@gmail.com").authorities(Role.ADMIN)))
                .andExpectAll(
                        status().is2xxSuccessful(),
                        view().name("book/addBook"),
                        model().attributeExists("book"),
                        model().attributeExists("authors"),
                        model().attributeExists("categories")
                );
    }

    @Test
    void bookEdit() throws Exception {
        mockMvc.perform(get("/books/1/update")
                        .with(user("test@gmail.com").authorities(Role.ADMIN))
                        .param(title, "test1")
                        .param(categoryId, "1")
                        .param(publishYear, "2022")
                        .param(description, "description")
                        .param(authorId, "1")
                        .param(number, "2")
                        .param(picture, "1.png")
                )
                .andExpectAll(
                        status().is2xxSuccessful(),
                        view().name("book/editBook"),
                        model().attributeExists("book"),
                        model().attributeExists("authors"),
                        model().attributeExists("categories")
                );
    }

    @Test
    void update() throws Exception {
        mockMvc.perform(post("/books/2/update")
                        .with(csrf())
                        .param(title, "Death on the Nile")
                        .param(authorId, "3")
                        .param(categoryId, "1")
                        .param(publishYear, "1977")
                        .param(description, "description")
                        .param(number, "5")
                        .param(picture, "new MockMultipartFile(\"test\", new byte[0])")

                )
                .andExpectAll(
                        status().is3xxRedirection(),
                        redirectedUrl("/books/2/update")
                );
    }

    @Test
    void deleteSuccessfully() throws Exception {
        mockMvc.perform(post("/books/1/delete")
                        .with(csrf()))
                .andExpectAll(
                        status().is3xxRedirection(),
                        redirectedUrl("/books?bookSuccessfullyDeleted=true")
                );
    }

    @Test
    void deleteNotExistBook() throws Exception {
        mockMvc.perform(post("/books/99999999999999/delete")
                        .with(csrf()))
                .andExpectAll(
                        status().is2xxSuccessful(),
                        view().name("error/error404")
                );
    }
}