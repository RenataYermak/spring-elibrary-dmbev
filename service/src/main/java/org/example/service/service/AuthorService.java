package org.example.service.service;


import lombok.RequiredArgsConstructor;
import org.example.service.database.repository.AuthorRepository;
import org.example.service.dto.bookDto.AuthorReadDto;
import org.example.service.mapper.bookMapper.AuthorReadMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuthorService {

    private final AuthorRepository authorRepository;
    private final AuthorReadMapper authorReadMapper;

    public List<AuthorReadDto> findAll() {
        return authorRepository.findAll().stream()
                .map(authorReadMapper::map)
                .toList();
    }
}