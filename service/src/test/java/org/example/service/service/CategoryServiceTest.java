package org.example.service.service;

import org.example.service.database.entity.Category;
import org.example.service.database.repository.CategoryRepository;
import org.example.service.dto.bookDto.CategoryReadDto;
import org.example.service.mapper.bookMapper.CategoryReadMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.example.service.util.ConstantUtil.ALL_2_CATEGORIES;
import static org.example.service.util.ConstantUtil.CATEGORY_ID_ONE;
import static org.example.service.util.ConstantUtil.CATEGORY_ID_TWO;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private CategoryReadMapper categoryReadMapper;
    @InjectMocks
    private CategoryService categoryService;

    @Test
    void checkFindAll() {
        var categories = List.of(getCategory(), getAnotherCategory());
        var expectedResult = List.of(getCategoryReadDto(), getAnotherCategoryReadDto());
        doReturn(categories).when(categoryRepository).findAll();
        doReturn(getCategoryReadDto(), getAnotherCategoryReadDto()).when(categoryReadMapper).map(any(Category.class));

        var actualResult = categoryService.findAll();

        assertThat(actualResult).hasSize(ALL_2_CATEGORIES);
        assertThat(expectedResult).isEqualTo(actualResult);
    }

    private Category getCategory() {
        return Category.builder()
                .name("Novel")
                .build();
    }

    private Category getAnotherCategory() {
        return Category.builder()
                .name("Drama")
                .build();
    }

    private CategoryReadDto getCategoryReadDto() {
        return new CategoryReadDto(
                CATEGORY_ID_ONE,
                "Novel"
        );
    }

    private CategoryReadDto getAnotherCategoryReadDto() {
        return new CategoryReadDto(
                CATEGORY_ID_TWO,
                "Drama"

        );
    }
}
