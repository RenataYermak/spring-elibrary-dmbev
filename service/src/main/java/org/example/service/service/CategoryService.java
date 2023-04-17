package org.example.service.service;

import lombok.RequiredArgsConstructor;
import org.example.service.database.repository.CategoryRepository;
import org.example.service.dto.bookDto.CategoryReadDto;
import org.example.service.mapper.bookMapper.CategoryReadMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryReadMapper categoryReadMapper;

    public List<CategoryReadDto> findAll() {
        return categoryRepository.findAll().stream()
                .map(categoryReadMapper::map)
                .toList();
    }
}

