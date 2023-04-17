package org.example.service.service;

import com.querydsl.core.types.Predicate;
import org.example.service.database.entity.Role;
import org.example.service.database.entity.User;
import org.example.service.database.querydsl.QPredicates;
import org.example.service.database.repository.UserRepository;
import org.example.service.dto.userDto.UserCreateEditDto;
import org.example.service.dto.userDto.UserFilter;
import org.example.service.dto.userDto.UserReadDto;
import org.example.service.mapper.userMapper.UserCreateEditMapper;
import org.example.service.mapper.userMapper.UserReadMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.example.service.database.entity.QUser.user;
import static org.example.service.util.ConstantUtil.ALL_2_USERS;
import static org.example.service.util.ConstantUtil.PAGE;
import static org.example.service.util.ConstantUtil.SIZE;
import static org.example.service.util.ConstantUtil.USER_ID_ONE;
import static org.example.service.util.ConstantUtil.USER_ID_TWO;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;


@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserReadMapper userReadMapper;

    @Mock
    private UserCreateEditMapper userCreateEditMapper;

    @InjectMocks
    private UserService userService;

    @Test
    void findAll() {
        var users = List.of(getUser(), getAnotherUser());
        var expectedResult = List.of(getUserReadDto(), getAnotherUserReadDto());
        doReturn(users).when(userRepository).findAll();
        doReturn(getUserReadDto(), getAnotherUserReadDto()).when(userReadMapper).map(any(User.class));

        var actualResult= userService.findAll();

        assertThat(actualResult).hasSize(ALL_2_USERS);
        assertThat(expectedResult).isEqualTo(actualResult);
    }

    @Test
    void findAllWithFilter() {
        var pageable = PageRequest.of(PAGE, SIZE);
        var filter = new UserFilter("renata@gmail.com", "Renata", "Yermak");
        var predicate = getPredicate(filter);
        Page<User> users = new PageImpl<>(List.of(getUser(), getAnotherUser()), pageable, SIZE);
        Page<UserReadDto> expectedResult = new PageImpl<>(List.of(
                getUserReadDto(), getAnotherUserReadDto()), pageable, 1);
        doReturn(users).when(userRepository).findAll(predicate, pageable);
        doReturn(getUserReadDto(), getAnotherUserReadDto()).when(userReadMapper).map(any(User.class));

        var actualResult = userService.findAll(filter, pageable);

        assertThat(actualResult).hasSize(SIZE);
        assertThat(expectedResult).isEqualTo(actualResult);
    }

    @Test
    void findById() {
        var user = getUser();
        var expectedResult = getUserReadDto();
        doReturn(Optional.of(user)).when(userRepository).findById(USER_ID_ONE);
        doReturn(expectedResult).when(userReadMapper).map(user);

        var actualResult = userService.findById(USER_ID_ONE);

        assertTrue(actualResult.isPresent());
        actualResult.ifPresent(actual -> assertEquals(expectedResult, actual));
    }

    @Test
    void create() {
        var userCreateEditDto = getUserCreateDto();
        var user = getUser();
        var expectedResult = getUserReadDto();
        doReturn(user).when(userCreateEditMapper).map(userCreateEditDto);
        doReturn(user).when(userRepository).save(user);
        doReturn(expectedResult).when(userReadMapper).map(user);

        var actualResult = userService.create(userCreateEditDto);

        assertThat(actualResult.getId()).isNotNull();
        assertThat(expectedResult).isEqualTo(actualResult);
    }

    @Test
    void update() {
        var user = getUser();
        var userCreateEditDto = getUpdatedUserDto();
        var updatedUser = getUpdatedUser();
        var expectedResult = getUpdatedUserReadDto();
        doReturn(Optional.of(user)).when(userRepository).findById(USER_ID_ONE);
        doReturn(updatedUser).when(userCreateEditMapper).map(userCreateEditDto, user);
        doReturn(updatedUser).when(userRepository).saveAndFlush(updatedUser);
        doReturn(expectedResult).when(userReadMapper).map(updatedUser);

        var actualResult = userService.update(USER_ID_ONE, userCreateEditDto);

        assertThat(Optional.of(expectedResult)).isEqualTo(actualResult);
    }

    @Test
    void delete() {
        var user = getUser();
        doReturn(Optional.of(user)).when(userRepository).findById(USER_ID_ONE);

        var expectedResult = userService.delete(USER_ID_ONE);

        assertTrue(expectedResult);
    }

    private User getUser() {
        return User.builder()
                .id(USER_ID_ONE)
                .firstname("Renata")
                .lastname("Yermak")
                .email("renata@gmail.com")
                .password("1212")
                .role(Role.ADMIN)
                .build();
    }

    private User getAnotherUser() {
        return User.builder()
                .id(USER_ID_TWO)
                .firstname("Alex")
                .lastname("Yermak")
                .email("alex@gmail.com")
                .password("1212")
                .role(Role.ADMIN)
                .build();
    }

    private UserReadDto getUserReadDto() {
        return new UserReadDto(
                USER_ID_ONE,
                "Renata",
                "Yermak",
                "renata@gmail.com",
                Role.ADMIN
        );
    }

    private UserReadDto getAnotherUserReadDto() {
        return new UserReadDto(
                USER_ID_TWO,
                "Alex",
                "Yermak",
                "alex@gmail.com",
                Role.ADMIN
        );
    }

    private UserCreateEditDto getUserCreateDto() {
        return new UserCreateEditDto(
                "Renata",
                "Yermak",
                "renata@gmail.com",
                "1212",
                Role.USER
        );
    }

    private UserCreateEditDto getUpdatedUserDto() {
        return new UserCreateEditDto(
                "Renata",
                "Yermak",
                "renata@gmail.com",
                "121212",
                Role.USER
        );
    }

    private User getUpdatedUser() {
        return User.builder()
                .id(USER_ID_ONE)
                .firstname("Renata")
                .lastname("Yermak")
                .email("renata@gmail.com")
                .password("121212")
                .role(Role.USER)
                .build();
    }

    private UserReadDto getUpdatedUserReadDto() {
        return new UserReadDto(
                USER_ID_ONE,
                "Renata",
                "Yermak",
                "renata@gmail.com",
                Role.USER
        );
    }

    private Predicate getPredicate(UserFilter filter) {
        return QPredicates.builder()
                .add(filter.firstname(), user.firstname::containsIgnoreCase)
                .add(filter.lastname(), user.lastname::containsIgnoreCase)
                .add(filter.email(), user.email::containsIgnoreCase)
                .build();
    }
}
