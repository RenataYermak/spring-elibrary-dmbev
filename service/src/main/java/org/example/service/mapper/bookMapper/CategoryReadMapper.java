package org.example.service.mapper.bookMapper;

import org.example.service.database.entity.Category;
import org.example.service.dto.bookDto.CategoryReadDto;
import org.example.service.mapper.Mapper;
import org.springframework.stereotype.Component;

@Component
public class CategoryReadMapper implements Mapper<Category, CategoryReadDto> {

    @Override
    public CategoryReadDto map(Category object) {
        return new CategoryReadDto(
                object.getId(),
                object.getName()
        );
    }
}
