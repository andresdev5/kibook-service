package ec.edu.espe.kibook.controller;

import ec.edu.espe.kibook.dto.BookDto;
import ec.edu.espe.kibook.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
public class BooksController {
    private final BookService bookService;

    @GetMapping
    public List<BookDto> getBooks() {
        return bookService.getBooks();
    }

    @GetMapping("/{id}")
    public BookDto getBookById(@PathVariable UUID id) {
        return new BookDto();
    }

    @PostMapping
    public BookDto createBook(@RequestBody @Validated({ BookDto.Create.class }) BookDto bookDto) {
        return bookService.createBook(bookDto);
    }

    @PutMapping("/{id}")
    public BookDto updateBook(@PathVariable UUID id, @RequestBody @Validated({ BookDto.Update.class }) BookDto bookDto) {
        return bookDto;
    }

    @DeleteMapping("/{id}")
    public void deleteBook(@PathVariable UUID id) {

    }

    @GetMapping("/search")
    public List<BookDto> searchBooks(@RequestParam String title) {
        return List.of();
    }
}
