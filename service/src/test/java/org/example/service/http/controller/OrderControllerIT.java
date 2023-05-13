package org.example.service.http.controller;

import lombok.RequiredArgsConstructor;
import org.example.service.integration.IntegrationTestBase;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@AutoConfigureMockMvc
@RequiredArgsConstructor
class OrderControllerIT extends IntegrationTestBase {

    private final MockMvc mockMvc;

    @Test
    void create() throws Exception {
        mockMvc.perform(post("/orders")
                        .with(csrf())
                        .param("userId", "1")
                        .param("bookId", "1")
                        .param("status", "ORDERED")
                        .param("type", "READING_ROOM")
                        .param("orderedDate", "2023-03-04T06:54:00")

                )
                .andExpectAll(
                        status().is3xxRedirection(),
                        redirectedUrl("/books")
                );
    }

    @Test
    void update() throws Exception {
        mockMvc.perform(get("/orders/1/update")
                        .param("userId", "1")
                        .param("bookId", "1")
                        .param("status", "RETURNED")
                        .param("type", "READING_ROOM"))
                .andExpectAll(
                        status().is3xxRedirection(),
                        redirectedUrl("/orders?orderSuccessfullyUpdated=true")
                );
    }

    @Test
    void deleteSuccessfully() throws Exception {
        mockMvc.perform(get("/orders/1/delete")
                        .with(csrf()))
                .andExpectAll(
                        status().is3xxRedirection(),
                        redirectedUrl("/orders?orderSuccessfullyDeleted=true")
                );
    }

    @Test
    void deleteNotExistOrder() throws Exception {
        mockMvc.perform(get("/orders/99999999999999/delete")
                        .with(csrf()))
                .andExpectAll(
                        status().is2xxSuccessful(),
                        view().name("error/error404")
                );
    }
}
