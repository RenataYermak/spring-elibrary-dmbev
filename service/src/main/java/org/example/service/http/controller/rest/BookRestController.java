package org.example.service.http.controller.rest;

import lombok.RequiredArgsConstructor;
import org.example.service.dto.PageResponse;
import org.example.service.dto.bookDto.BookCreateEditDto;
import org.example.service.dto.bookDto.BookFilter;
import org.example.service.dto.bookDto.BookReadDto;
import org.example.service.service.BookService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.springframework.http.ResponseEntity.noContent;
import static org.springframework.http.ResponseEntity.notFound;

@RestController
@RequestMapping("/api/v1/books")
@RequiredArgsConstructor
public class BookRestController {

    private final BookService bookService;

    @GetMapping("/{id}/picture")
    public ResponseEntity<byte[]> findPicture(@PathVariable("id") Long id) {
        return bookService.findPicture(id)
                .map(content -> ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE)
                        .contentLength(content.length)
                        .body(content))
                .orElseGet(notFound()::build);
    }

    @GetMapping
    public PageResponse<BookReadDto> findAll(BookFilter filter,
                                             Pageable pageable) {
        var page = bookService.findAll(filter, pageable);
        return PageResponse.of(page);
    }

    @GetMapping("/{id}")
    public BookReadDto findById(@PathVariable("id") Long id) {
        return bookService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookReadDto create(@Validated @RequestBody BookCreateEditDto book) {
        return bookService.create(book);
    }

    @GetMapping("/{id}/update")
    public Optional<BookReadDto> bookEdit(@PathVariable(value = "id") Long bookId) {
        return bookService.findById(bookId);
    }

    @PutMapping("/{id}/update")
    public BookReadDto update(@PathVariable("id") Long id,
                              @Validated BookCreateEditDto book) {
        return bookService.update(id, book)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        return bookService.delete(id)
                ? noContent().build()
                : notFound().build();
    }
}
