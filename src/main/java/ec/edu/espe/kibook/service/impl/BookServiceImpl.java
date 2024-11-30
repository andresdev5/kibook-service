package ec.edu.espe.kibook.service.impl;

import ec.edu.espe.kibook.dto.AuthorDto;
import ec.edu.espe.kibook.dto.BookDto;
import ec.edu.espe.kibook.dto.GenreDto;
import ec.edu.espe.kibook.entity.Author;
import ec.edu.espe.kibook.entity.Book;
import ec.edu.espe.kibook.entity.BookStatus;
import ec.edu.espe.kibook.entity.Genre;
import ec.edu.espe.kibook.repository.AuthorRepository;
import ec.edu.espe.kibook.repository.BookRepository;
import ec.edu.espe.kibook.repository.GenreRepository;
import ec.edu.espe.kibook.service.BookService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final GenreRepository genreRepository;
    private final AuthorRepository authorRepository;
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
        Set<Genre> genres = genreRepository.findAllByIdIn(bookDto.getGenres().stream()
                    .map(GenreDto::getId)
                    .collect(Collectors.toSet()));
        Set<Author> authors = authorRepository.findAllByIdIn(bookDto.getAuthors().stream()
                    .map(AuthorDto::getId)
                    .collect(Collectors.toSet()));

        if (genres.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Se necesita al menos un género");
        }

        if (authors.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Se necesita al menos un autor");
        }

        Book book = Book.builder()
            .title(bookDto.getTitle())
            .isbn(bookDto.getIsbn())
            .synopsis(bookDto.getSynopsis())
            .year(bookDto.getYear())
            .publisher(bookDto.getPublisher())
            .status(BookStatus.AVAILABLE)
            .genres(genres)
            .authors(authors)
            .build();

        if (bookDto.getImage() == null) {
            book.setImage("https://placehold.co/300x400");
        } else {
            book.setImage(bookDto.getImage());
        }

        Book saved = bookRepository.save(book);
        return modelMapper.map(saved, BookDto.class);
    }

    @Override
    public BookDto updateBook(UUID id, BookDto bookDto) {
        return null;
    }

    @Override
    public void deleteBook(UUID id) {
        Book book = bookRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No se ha encontrado el libro solicitado"));

        bookRepository.delete(book);
    }

    @Override
    public List<BookDto> searchBooks(String title) {
        return List.of();
    }
}
