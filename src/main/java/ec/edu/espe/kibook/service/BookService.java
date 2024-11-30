package ec.edu.espe.kibook.service;

import ec.edu.espe.kibook.dto.BookDto;
import ec.edu.espe.kibook.dto.BookSearchParamsDto;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;

public interface BookService {
    List<BookDto> getBooks();
    BookDto getBookById(UUID id);
    BookDto createBook(BookDto bookDto);
    BookDto updateBook(UUID id, BookDto bookDto);
    void deleteBook(UUID id);
    Page<BookDto> searchBooks(BookSearchParamsDto title);
}
