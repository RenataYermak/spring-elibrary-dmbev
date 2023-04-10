package org.example.service.service;

import lombok.RequiredArgsConstructor;
import org.example.service.anotation.IT;
import org.example.service.database.entity.Role;
import org.example.service.dto.userDto.UserCreateEditDto;
import org.example.service.dto.userDto.UserReadDto;
import org.example.service.integration.IntegrationTestBase;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.example.service.util.ConstantUtil.USER_ID_ONE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

@IT
@RequiredArgsConstructor
public class UserServiceIT extends IntegrationTestBase {

    private final UserService userService;

    @Test
    void findAll() {
        List<UserReadDto> result = userService.findAll();
        assertThat(result).hasSize(4);
    }

    @Test
    void findById() {
        Optional<UserReadDto> maybeUser = userService.findById(USER_ID_ONE);
        assertTrue(maybeUser.isPresent());
        maybeUser.ifPresent(user -> assertEquals("renatayermak@gmail.com", user.getEmail()));
    }

    @Test
    void create() {
        UserCreateEditDto userDto = new UserCreateEditDto(
                "Renata",
                "Yermak",
                "renata@gmail.com",
                "1212",
                Role.USER
        );
        UserReadDto actualResult = userService.create(userDto);
        assertEquals(userDto.getFirstname(), actualResult.getFirstname());
        assertEquals(userDto.getLastname(), actualResult.getLastname());
        assertEquals(userDto.getEmail(), actualResult.getEmail());
        assertSame(userDto.getRole(), actualResult.getRole());
    }

    @Test
    void update() {
        UserCreateEditDto userDto = new UserCreateEditDto(
                "Renata",
                "Yermak",
                "renata@gmail.com",
                "1212",
                Role.USER
        );
        Optional<UserReadDto> actualResult = userService.update(USER_ID_ONE, userDto);
        assertTrue(actualResult.isPresent());

        actualResult.ifPresent(
                user -> {
                    assertEquals(userDto.getFirstname(), user.getFirstname());
                    assertEquals(userDto.getLastname(), user.getLastname());
                    assertEquals(userDto.getEmail(), user.getEmail());
                    assertSame(userDto.getRole(), user.getRole());
                }
        );
    }

    @Test
    void delete() {
        assertFalse(userService.delete(-2345678L));
        assertTrue(userService.delete(USER_ID_ONE));
    }
}
