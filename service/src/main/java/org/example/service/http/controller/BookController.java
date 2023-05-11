package org.example.service.http.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.service.database.entity.Book;
import org.example.service.database.repository.BookRepository;
import org.example.service.dto.PageResponse;
import org.example.service.dto.bookDto.BookCreateEditDto;
import org.example.service.dto.bookDto.BookFilter;
import org.example.service.service.AuthorService;
import org.example.service.service.BookService;
import org.example.service.service.CategoryService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Controller
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;
    private final AuthorService authorService;
    private final CategoryService categoryService;
    private final BookRepository bookRepository;

    @GetMapping
    public String findAll(Model model,
                          BookFilter filter,
                          @PageableDefault Pageable pageable) {
        var page = bookService.findAll(filter, pageable);
        model.addAttribute("books", PageResponse.of(page));
        model.addAttribute("filter", filter);
        return "book/books";
    }

    @GetMapping("/{id}")
    public String bookFindById(@PathVariable("id") Long bookId, Model model) {
        return bookService.findById(bookId)
                .map(book -> {
                    model.addAttribute("book", book);
                    return "book/book";
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/add")
    public String addBook(Model model,
                          @ModelAttribute("book") BookCreateEditDto book) {
        model.addAttribute("book", book);
        model.addAttribute("authors", authorService.findAll());
        model.addAttribute("categories", categoryService.findAll());
        return "book/addBook";
    }

    @PostMapping
    public String create(@ModelAttribute @Validated BookCreateEditDto book,
                         BindingResult bindingResult,
                         RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("book", book);
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            return "redirect:/books/add";
        }
        bookService.create(book);
        redirectAttributes.addAttribute("bookSuccessfullyCreated", "true");
        return "redirect:/books/add";
    }

    @GetMapping("/{id}/update")
    public String update(@PathVariable(value = "id") long bookId, Model model) {
        if (!bookRepository.existsById(bookId)) {
            return "redirect:/books";
        }
        Optional<Book> book = bookRepository.findById(bookId);
        List<Book> result = new ArrayList<>();
        book.ifPresent(result::add);
        model.addAttribute("authors", authorService.findAll());
        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("book", result);
        return "book/editBook";
    }

    @PostMapping("/{id}/update")
    public String update(@PathVariable("id") Long id,
                         @ModelAttribute @Validated BookCreateEditDto book,
                         BindingResult bindingResult,
                         RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("book", book);
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            return "redirect:/books/{id}/update";
        }
        bookService.update(id, book);
        redirectAttributes.addAttribute("bookSuccessfullyUpdated", "true");
        return "redirect:/books/{id}/update";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable("id") Long bookId,
                         RedirectAttributes redirectAttributes) {
        if (!bookService.delete(bookId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        redirectAttributes.addAttribute("bookSuccessfullyDeleted", "true");
        return "redirect:/books";
    }
}