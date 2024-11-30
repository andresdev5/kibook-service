package ec.edu.espe.kibook.service.impl;

import ec.edu.espe.kibook.dto.AuthorDto;
import ec.edu.espe.kibook.entity.Author;
import ec.edu.espe.kibook.repository.AuthorRepository;
import ec.edu.espe.kibook.repository.BookRepository;
import ec.edu.espe.kibook.service.AuthorService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {
    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<AuthorDto> getAuthors() {
        return authorRepository.findAll()
                .stream()
                .map(author -> modelMapper.map(author, AuthorDto.class))
                .toList();
    }

    @Override
    public AuthorDto getAuthorById(UUID id) {
        return modelMapper.map(authorRepository
                .findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No se ha encontrado el autor solicitado")), AuthorDto.class);
    }

    @Override
    public AuthorDto createAuthor(AuthorDto authorDto) {
        Author author = Author.builder()
                .firstName(authorDto.getFirstName())
                .lastName(authorDto.getLastName())
                .pseudonym(authorDto.getPseudonym())
                .biography(authorDto.getBiography())
                .birthDate(authorDto.getBirthDate())
                .build();

        return modelMapper.map(authorRepository.save(author), AuthorDto.class);
    }

    @Override
    public AuthorDto updateAuthor(UUID id, AuthorDto authorDto) {
        return null;
    }

    @Override
    public void deleteAuthor(UUID id) {
        boolean dependentBooksExists = bookRepository.existsAllByAuthorsIn(List.of(authorRepository.findById(id).orElseThrow()));

        if (dependentBooksExists) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No se puede eliminar un autor que tiene libros asociados, elimina los libros primero");
        }

        authorRepository.deleteById(id);
    }
}
