package org.example.service.http.controller;

import lombok.RequiredArgsConstructor;
import org.example.service.integration.IntegrationTestBase;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@RequiredArgsConstructor
class OrderControllerIT extends IntegrationTestBase {

    private final MockMvc mockMvc;

//    @Test
//    void findAll() throws Exception {
//        mockMvc.perform(get("/orders")
//                        .with(user("test@gmail.com").authorities(Role.USER))
//                        .with(csrf()))
//                .andExpectAll(
//                        status().is2xxSuccessful(),
//                        view().name("order/order"),
//                        model().attributeExists("filter"),
//                        model().attributeExists("orders")
//                );
//    }
//
//    @Test
//    void findAllByUserId() throws Exception {
//        mockMvc.perform(get("/orders/1")
//                        .with(user("test@gmail.com").authorities(Role.ADMIN)))
//                .andExpectAll(
//                        status().is2xxSuccessful(),
//                        view().name("order/orders"),
//                        model().attributeExists("orders"),
//                        model().attributeExists("filter"),
//                        model().attributeExists("userId")
//                );
//    }
//
//    @Test
//    void findById() throws Exception {
//        mockMvc.perform(get("/orders/1")
//                        .with(user("test@gmail.com").authorities(Role.ADMIN)))
//                .andExpectAll(
//                        status().is2xxSuccessful(),
//                        view().name("order/orders"),
//                        model().attributeExists("order")
//                );
//    }
//
//    @Test
//    void create() throws Exception {
//        LocalDateTime date = LocalDateTime.now();
//        String formattedDate = formatLocalDateTime(date);
//        mockMvc.perform(post("/orders")
//                        .with(csrf())
//                        .param(userId, "1")
//                        .param(bookId, "1")
//                        .param(status, "ORDERED")
//                        .param(type, "READING_ROOM")
//                        .param(orderedDate, formattedDate)
//                )
//                .andExpectAll(
//                        status().is3xxRedirection(),
//                        redirectedUrl("/orders")
//                );
//    }
//
//    @Test
//    void update() throws Exception {
//        mockMvc.perform(get("/orders/1/update")
//                        .param(userId, "1")
//                        .param(bookId, "1")
//                        .param(status, "RETURNED")
//                        .param(type, "READING_ROOM")
//                        .param(orderedDate, String.valueOf(LocalDateTime.now()))
//                        .param(returnedDate, String.valueOf(LocalDateTime.now())))
//                .andExpectAll(
//                        status().is2xxSuccessful(),
//                        redirectedUrl("/orders")
//                );
//    }

    @Test
    void delete() throws Exception {
        mockMvc.perform(get("/orders/1/delete")
                        .with(csrf()))
                .andExpectAll(
                        status().is3xxRedirection(),
                        redirectedUrl("/orders?orderSuccessfullyDeleted=true")
                );
    }
}
