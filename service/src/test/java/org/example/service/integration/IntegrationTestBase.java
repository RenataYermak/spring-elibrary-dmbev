package org.example.service.integration;

import org.example.service.anotation.IT;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.containers.PostgreSQLContainer;

@IT
@Sql({
        "classpath:sql/data.sql"
})
@WithMockUser(username = "test@gmail.com", password = "test1234", authorities = {"ADMIN", "USER"})
public abstract class IntegrationTestBase {

    private static final PostgreSQLContainer<?> container = new PostgreSQLContainer<>("postgres:15");

    @BeforeAll
    static void init() {
        container.start();
    }

    @DynamicPropertySource
    static void postgresProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", container::getJdbcUrl);
    }
}
