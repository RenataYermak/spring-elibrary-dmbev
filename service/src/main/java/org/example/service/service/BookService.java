package org.example.service.service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.example.service.database.entity.Book;
import org.example.service.database.querydsl.QPredicates;
import org.example.service.database.repository.BookRepository;
import org.example.service.dto.bookDto.BookCreateEditDto;
import org.example.service.dto.bookDto.BookFilter;
import org.example.service.dto.bookDto.BookReadDto;
import org.example.service.mapper.bookMapper.BookCreateEditMapper;
import org.example.service.mapper.bookMapper.BookReadMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

import static org.example.service.database.entity.QBook.book;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookService {

    private final BookRepository bookRepository;
    private final BookReadMapper bookReadMapper;
    private final BookCreateEditMapper bookCreateEditMapper;
    private final ImageService imageService;

    public Page<BookReadDto> findAll(BookFilter filter, Pageable pageable) {
        var predicate = QPredicates.builder()
                .add(filter.publishYear(), book.publishYear::eq)
                .add(filter.category(), book.category.name::eq)
                .add(filter.author(), book.author.name::eq)
                .build();

        return bookRepository.findAll(predicate, pageable)
                .map(bookReadMapper::map);
    }

    public Optional<BookReadDto> findById(Long id) {
        return bookRepository.findById(id)
                .map(bookReadMapper::map);
    }

    public List<BookReadDto> findAll() {
        return bookRepository.findAll().stream()
                .map(bookReadMapper::map)
                .toList();
    }

    @SneakyThrows
    private void uploadImage(MultipartFile image) {
        if (!image.isEmpty()) {
            imageService.upload(image.getOriginalFilename(), image.getInputStream());
        }
    }

    public Optional<byte[]> findPicture(Long id) {
        return bookRepository.findById(id)
                .map(Book::getPicture)
                .filter(StringUtils::hasText)
                .flatMap(imageService::get);
    }

    @Transactional
    public BookReadDto create(BookCreateEditDto bookDto) {
        return Optional.of(bookDto)
                .map(dto -> {
                    uploadImage(dto.getPicture());
                    return bookCreateEditMapper.map(dto);
                })
                .map(bookRepository::save)
                .map(bookReadMapper::map)
                .orElseThrow();
    }

    @Transactional
    public Optional<BookReadDto> update(Long id, BookCreateEditDto bookDto) {
        return bookRepository.findById(id)
                .map(entity -> {
                    uploadImage(bookDto.getPicture());
                    return bookCreateEditMapper.map(bookDto, entity);
                })
                .map(bookRepository::saveAndFlush)
                .map(bookReadMapper::map);
    }

    @Transactional
    public boolean delete(Long id) {
        return bookRepository.findById(id)
                .map(entity -> {
                    bookRepository.delete(entity);
                    bookRepository.flush();
                    return true;
                })
                .orElse(false);
    }
}