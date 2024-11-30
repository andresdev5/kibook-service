package ec.edu.espe.kibook.service.impl;

import ec.edu.espe.kibook.dto.AuthorDto;
import ec.edu.espe.kibook.dto.BookDto;
import ec.edu.espe.kibook.dto.BookSearchParamsDto;
import ec.edu.espe.kibook.dto.GenreDto;
import ec.edu.espe.kibook.entity.Author;
import ec.edu.espe.kibook.entity.Book;
import ec.edu.espe.kibook.entity.BookStatus;
import ec.edu.espe.kibook.entity.Genre;
import ec.edu.espe.kibook.repository.AuthorRepository;
import ec.edu.espe.kibook.repository.BookRepository;
import ec.edu.espe.kibook.repository.GenreRepository;
import ec.edu.espe.kibook.service.BookService;
import ec.edu.espe.kibook.specification.BookSpecification;
import ec.edu.espe.kibook.util.TextUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.imgscalr.Scalr;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final Logger logger = Logger.getLogger(BookServiceImpl.class.getName());
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
        return modelMapper.map(bookRepository
            .findById(id)
            .orElseThrow(() -> new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "No se ha encontrado el libro solicitado")), BookDto.class);
    }

    @Override
    @Transactional
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
            .image("https://placehold.co/400x600")
            .genres(genres)
            .authors(authors)
            .build();

        Book saved = bookRepository.save(book);

        if (bookDto.getImage() != null) {
            saved.setImage(uploadBookImage(saved, bookDto.getImage()));
            bookRepository.save(saved);
        } else {
            saved.setImage(uploadDefaultBookImage(saved));
            bookRepository.save(saved);
        }

        return modelMapper.map(saved, BookDto.class);
    }

    @Override
    public BookDto updateBook(UUID id, BookDto bookDto) {
        return null;
    }

    @Override
    public void deleteBook(UUID id) {
        Book book = bookRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "No se ha encontrado el libro solicitado"));

        bookRepository.delete(book);
    }

    @Override
    public Page<BookDto> searchBooks(BookSearchParamsDto params) {
        Specification<Book> filters = Specification
                .where(StringUtils.isBlank(params.getTitle()) ? null : BookSpecification.titleContains(params.getTitle()));

        Sort sort = Sort.by(
                params.getOrder() == BookSearchParamsDto.Order.ASC
                        ? Sort.Order.asc(params.getSortField().getValue())
                        : Sort.Order.desc(params.getSortField().getValue()));
        Pageable pageable = PageRequest.of(params.getPage(), params.getSize(), sort);
        Page<Book> books = bookRepository.findAll(filters, pageable);

        return books.map(book -> modelMapper.map(book, BookDto.class));
    }

    private String uploadDefaultBookImage(Book book) {
        try {
            String placeholder = String.format("https://placehold.co/400x600/ced6e0/57606f/jpg?text=%s",
                    UriUtils.encodePath(TextUtils.wrapText(book.getTitle(), 20, "\n"), "UTF-8"));
            URL url = new URL(placeholder);
            InputStream is = url.openStream();
            byte[] imageBytes = IOUtils.toByteArray(is);

            return uploadBookImage(book, imageBytes);
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.severe("Error al cargar la imagen por defecto: " + ex.getMessage());

            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "Error al guardar información del libro");
        }
    }

    private String uploadBookImage(Book book, byte[] image) {
        String timestamp = String.valueOf(System.currentTimeMillis());
        String filename = String.format("book-%s-%s.jpg", book.getId(), timestamp);
        Path path = Path.of("uploads", "books-posters", filename);

        try {
            int width = 400;
            int height = 600;
            BufferedImage inputImage = ImageIO.read(new ByteArrayInputStream(image));
            Scalr.Mode mode = (double) width / (double) height >= (double) inputImage.getWidth()
                            / (double) inputImage.getHeight() ? Scalr.Mode.FIT_TO_WIDTH
                    : Scalr.Mode.FIT_TO_HEIGHT;

            BufferedImage bufferedImage = Scalr.resize(inputImage, Scalr.Method.QUALITY, mode, width, height);

            int x = 0;
            int y = 0;

            if (mode == Scalr.Mode.FIT_TO_WIDTH) {
                y = (bufferedImage.getHeight() - height) / 2;
            } else if (mode == Scalr.Mode.FIT_TO_HEIGHT) {
                x = (bufferedImage.getWidth() - width) / 2;
            }

            bufferedImage = Scalr.crop(bufferedImage, x, y, width, height);
            File outputFile = path.toFile();
            Files.createDirectories(path.getParent());
            ImageIO.write(bufferedImage, "jpg", outputFile);

            return "/" + path.toString().replace("\\", "/");
        } catch (IOException e) {
            logger.severe("Error al procesar la imagen del libro: " + e.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "Error al procesar la imagen del libro");
        }
    }
}
