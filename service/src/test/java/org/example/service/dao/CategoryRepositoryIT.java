package org.example.service.dao;

import org.example.service.integration.IntegrationTestBase;
import org.example.service.util.EntityTestUtil;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.example.service.util.ConstantUtil.ALL_CATEGORIES;
import static org.example.service.util.ConstantUtil.CATEGORY_ID_ONE;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class CategoryRepositoryIT extends IntegrationTestBase {

    private final CategoryRepository categoryRepository = context.getBean(CategoryRepository.class);

    @Test
    void findById() {
        var actualCategory = categoryRepository.findById(CATEGORY_ID_ONE);
        session.clear();

        assertThat(actualCategory).isPresent();
        assertThat(actualCategory.get().getName()).isEqualTo("Drama");
    }

    @Test
    void findAll() {
        var categories = categoryRepository.findAll();
        session.clear();

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
        session.clear();

        var deletedCategory = categoryRepository.findById(category.getId());

        assertThat(deletedCategory).isEmpty();
    }

    @Test
    void update() {
        var expectedCategory = categoryRepository.findById(CATEGORY_ID_ONE).get();
        expectedCategory.setName("Science");
        categoryRepository.update(expectedCategory);
        session.clear();

        var actualCategory = categoryRepository.findById(CATEGORY_ID_ONE);

        assertThat(actualCategory).isPresent();
        assertThat(actualCategory.get().getName()).isEqualTo("Science");
    }
}
