package org.example.service.repository;

import lombok.RequiredArgsConstructor;
import org.example.service.database.repository.CategoryRepository;
import org.example.service.integration.IntegrationTestBase;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.example.service.util.ConstantUtil.ALL_CATEGORIES;
import static org.example.service.util.ConstantUtil.CATEGORY_ID_ONE;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@RequiredArgsConstructor
public class CategoryRepositoryIT extends IntegrationTestBase {

    private final CategoryRepository categoryRepository;
    private final EntityManager entityManager;

    @Test
    void findById() {
        var actualCategory = categoryRepository.findById(CATEGORY_ID_ONE);
        entityManager.clear();

        assertThat(actualCategory).isPresent();
        assertThat(actualCategory.get().getName()).isEqualTo("Drama");
    }

    @Test
    void findAll() {
        var categories = categoryRepository.findAll();
        entityManager.clear();

        assertNotNull(categories);
        assertThat(categories).hasSize(ALL_CATEGORIES);
    }

    @Test
    void save() {
        var category = EntityTestUtil.getCategory();

        var actualCategory = categoryRepository.save(category);

        assertThat(actualCategory.getId()).isNotNull();
    }

    @Test
    void delete() {
        var category = EntityTestUtil.getCategory();
        categoryRepository.save(category);

        categoryRepository.delete(category);
        entityManager.flush();
        entityManager.clear();

        var deletedCategory = categoryRepository.findById(category.getId());

        assertThat(deletedCategory).isEmpty();
    }

    @Test
    void update() {
        var expectedCategory = categoryRepository.findById(CATEGORY_ID_ONE).get();
        expectedCategory.setName("Science");
        categoryRepository.saveAndFlush(expectedCategory);
        entityManager.clear();

        var actualCategory = categoryRepository.findById(CATEGORY_ID_ONE);

        assertThat(actualCategory).isPresent();
        assertThat(actualCategory.get().getName()).isEqualTo("Science");
    }
}
