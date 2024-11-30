package ec.edu.espe.kibook.service.impl;

import ec.edu.espe.kibook.dto.BookDto;
import ec.edu.espe.kibook.entity.Book;
import ec.edu.espe.kibook.repository.BookRepository;
import ec.edu.espe.kibook.service.BookService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<BookDto> getBooks() {
        return bookRepository.findAll()
            .stream()
            .map(book -> modelMapper.map(book, BookDto.class))
            .toList();
    }

    @Override
    public BookDto getBookById(UUID id) {
        return null;
    }

    @Override
    public BookDto createBook(BookDto bookDto) {
        Book book = Book.builder()
            .title(bookDto.getTitle())
            .build();

        Book saved = bookRepository.save(book);

        return modelMapper.map(bookRepository.save(book), BookDto.class);
    }

    @Override
    public BookDto updateBook(UUID id, BookDto bookDto) {
        return null;
    }

    @Override
    public void deleteBook(UUID id) {

    }

    @Override
    public List<BookDto> searchBooks(String title) {
        return List.of();
    }
}
