package org.example.service.repository;

import lombok.RequiredArgsConstructor;
import org.example.service.database.entity.Role;
import org.example.service.database.repository.UserRepository;
import org.example.service.dto.userDto.UserFilter;
import org.example.service.integration.IntegrationTestBase;
import org.example.service.util.EntityTestUtil;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.example.service.util.ConstantUtil.ALL_USERS;
import static org.example.service.util.ConstantUtil.USER_ID_ONE;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@RequiredArgsConstructor
public class UserRepositoryIT extends IntegrationTestBase {

    private final UserRepository userRepository;
    private final EntityManager entityManager;

    @Test
    void findById() {
        var actualUser = userRepository.findById(USER_ID_ONE);
        entityManager.clear();

        assertThat(actualUser).isPresent();
        assertThat(actualUser.get().getEmail()).isEqualTo("renatayermak@gmail.com");
    }

    @Test
    void findAll() {
        var users = userRepository.findAll();
        entityManager.clear();

        assertNotNull(users);
        assertThat(users).hasSize(ALL_USERS);
    }

    @Test
    void save() {
        var user = EntityTestUtil.getUser();

        var actualUser = userRepository.save(user);

        assertThat(actualUser.getId()).isNotNull();
    }

    @Test
    void delete() {
        var user = EntityTestUtil.getUser();
        userRepository.save(user);

        userRepository.deleteById(user.getId());
        entityManager.flush();
        entityManager.clear();

        var deletedUser = userRepository.findById(user.getId());

        assertThat(deletedUser).isEmpty();

    }

    @Test
    void update() {
        var expectedUser = userRepository.findById(USER_ID_ONE).get();
        expectedUser.setEmail("newemail@gmail.com");
        userRepository.saveAndFlush(expectedUser);
        entityManager.clear();

        var actualUser = userRepository.findById(USER_ID_ONE);

        assertThat(actualUser).isPresent();
        assertThat(actualUser.get().getEmail()).isEqualTo("newemail@gmail.com");
    }

    @Test
    void findUsersByQueryWithAllParameters() {
        var filter = UserFilter.builder()
                .email("alex@gmail.com")
                .firstname("Alex")
                .lastname("Yermak")
                .build();

        var users = userRepository.findAllByFilter(filter);

        assertNotNull(users);
        assertThat(users).hasSize(1);
        assertThat(users.get(0).getEmail()).isEqualTo("alex@gmail.com");
        assertThat(users.get(0).getFirstname()).isEqualTo("Alex");
        assertThat(users.get(0).getLastname()).isEqualTo("Yermak");
        assertThat(users.get(0).getRole()).isEqualTo(Role.USER);
    }

    @Test
    void findUsersByQueryWithOneParameters() {
        var filter = UserFilter.builder()
                .lastname("Yermak")
                .build();

        var users = userRepository.findAllByFilter(filter);

        assertNotNull(users);
        assertThat(users).hasSize(2);
    }

    @Test
    void findUsersByQueryWithoutParameters() {
        var filter = UserFilter.builder()
                .build();

        var users = userRepository.findAllByFilter(filter);

        assertNotNull(users);
        assertThat(users).hasSize(userRepository.findAll().size());
    }
}
