package org.example.service.dao;

import lombok.RequiredArgsConstructor;
import org.example.service.integration.IntegrationTestBase;
import org.example.service.util.EntityTestUtil;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.example.service.util.ConstantUtil.ALL_AUTHORS;
import static org.example.service.util.ConstantUtil.AUTHOR_ID_ONE;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@RequiredArgsConstructor
public class AuthorRepositoryIT extends IntegrationTestBase {

    private final AuthorRepository authorRepository;
    private final EntityManager entityManager;

    @Test
    void findById() {
        var actualAuthor = authorRepository.findById(AUTHOR_ID_ONE);
        entityManager.clear();

        assertThat(actualAuthor).isPresent();
        assertThat(actualAuthor.get().getName()).isEqualTo("Stephan King");
    }

    @Test
    void findAll() {
        var authors = authorRepository.findAll();
        entityManager.clear();

        assertNotNull(authors);
        assertThat(authors).hasSize(ALL_AUTHORS);
    }

    @Test
    void save() {
        var author = EntityTestUtil.getAuthor();

        var actualAuthor = authorRepository.save(author);

        assertThat(actualAuthor.getId()).isNotNull();
    }

    @Test
    void delete() {
        var author = EntityTestUtil.getAuthor();
        authorRepository.save(author);
        authorRepository.delete(author);
        entityManager.clear();

        var deletedAuthor = authorRepository.findById(author.getId());

        assertThat(deletedAuthor).isEmpty();
    }

    @Test
    void update() {
        var expectedAuthor = authorRepository.findById(AUTHOR_ID_ONE).get();
        expectedAuthor.setName("Ernest Hemingway");
        authorRepository.update(expectedAuthor);
        entityManager.clear();

        var actualAuthor = authorRepository.findById(AUTHOR_ID_ONE);

        assertThat(actualAuthor).isPresent();
        assertThat(actualAuthor.get().getName()).isEqualTo("Ernest Hemingway");
    }
}
