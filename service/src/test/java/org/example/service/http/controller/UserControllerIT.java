package org.example.service.http.controller;

import lombok.RequiredArgsConstructor;
import org.example.service.database.entity.Role;
import org.example.service.integration.IntegrationTestBase;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static org.example.service.dto.userDto.UserCreateEditDto.Fields.email;
import static org.example.service.dto.userDto.UserCreateEditDto.Fields.firstname;
import static org.example.service.dto.userDto.UserCreateEditDto.Fields.lastname;
import static org.example.service.dto.userDto.UserCreateEditDto.Fields.rawPassword;
import static org.example.service.dto.userDto.UserCreateEditDto.Fields.role;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@AutoConfigureMockMvc
@RequiredArgsConstructor
public class UserControllerIT extends IntegrationTestBase {

    private final MockMvc mockMvc;

    @Test
    void findAll() throws Exception {
        mockMvc.perform(get("/users")
                        .with(user("test@gmail.com").authorities(Role.ADMIN)))
                .andExpectAll(
                        status().is2xxSuccessful(),
                        view().name("user/users"),
                        model().attributeExists("users")
                );
    }

    @Test
    void findById() throws Exception {
        mockMvc.perform(get("/users/1"))
                .andExpectAll(
                        status().is2xxSuccessful(),
                        view().name("user/user"),
                        model().attributeExists("user"),
                        model().attributeExists("roles")
                );
    }

    @Test
    void create() throws Exception {
        mockMvc.perform(post("/users")
                        .with(user("test@gmail.com").authorities(Role.ADMIN))
                        .param(firstname, "test")
                        .param(lastname, "test")
                        .param(email, "test1@gmail.com")
                        .param(rawPassword, "test1212")
                        .param(role, "ADMIN")
                )
                .andExpectAll(
                        status().is3xxRedirection(),
                        redirectedUrl("/users")
                );
    }

    @Test
    void registration() throws Exception {
        mockMvc.perform(get("/users/registration"))
                .andExpectAll(
                        status().is2xxSuccessful(),
                        view().name("user/registration"),
                        model().attributeExists("user"),
                        model().attributeExists("roles")
                );
    }

    @Test
    void userEdit() throws Exception {
        mockMvc.perform(get("/users/1/update")
                        .param(firstname, "Renata")
                        .param(lastname, "Yermak")
                        .param(email, "renatayermak@gmail.com")
                        .param(rawPassword, "12121997")
                        .param(role, "ADMIN")
                )
                .andExpectAll(
                        status().is2xxSuccessful(),
                        view().name("user/user"),
                        model().attributeExists("user"),
                        model().attributeExists("roles")
                );
    }

    @Test
    void update() throws Exception {
        mockMvc.perform(post("/users/1/update")
                        .with(user("test@gmail.com").authorities(Role.ADMIN))
                        .param(firstname, "Renata")
                        .param(lastname, "Yermak")
                        .param(email, "renatayermak@gmail.com")
                        .param(rawPassword, "12121997")
                        .param(role, "USER")
                )
                .andExpectAll(
                        status().is3xxRedirection(),
                        redirectedUrl("/users/1")
                );
    }

    @Test
    void delete() throws Exception {
        mockMvc.perform(post("/users/1/delete")
                .with(user("test@gmail.com").authorities(Role.ADMIN)))
                .andExpectAll(
                        status().is3xxRedirection(),
                        redirectedUrl("/users")
                );
    }
}
