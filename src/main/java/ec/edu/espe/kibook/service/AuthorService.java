package ec.edu.espe.kibook.service;

import ec.edu.espe.kibook.dto.AuthorDto;

import java.util.List;
import java.util.UUID;

public interface AuthorService {
    List<AuthorDto> getAuthors();
    AuthorDto getAuthorById(UUID id);
    AuthorDto createAuthor(AuthorDto authorDto);
    AuthorDto updateAuthor(UUID id, AuthorDto authorDto);
    void deleteAuthor(UUID id);
}
