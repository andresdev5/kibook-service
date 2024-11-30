package ec.edu.espe.kibook.service.impl;

import ec.edu.espe.kibook.dto.GenreDto;
import ec.edu.espe.kibook.entity.Genre;
import ec.edu.espe.kibook.repository.BookRepository;
import ec.edu.espe.kibook.repository.GenreRepository;
import ec.edu.espe.kibook.service.GenreService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GenreServiceImpl implements GenreService {
    private final GenreRepository genreRepository;
    private final BookRepository bookRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<GenreDto> getGenres() {
        return genreRepository.findAll()
                .stream()
                .map(genre -> modelMapper.map(genre, GenreDto.class))
                .toList();
    }

    @Override
    public GenreDto getGenreById(UUID id) {
        return modelMapper.map(genreRepository
                .findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No se ha encontrado el genero especificado")), GenreDto.class);
    }

    @Override
    public GenreDto createGenre(GenreDto genreDto) {
        Genre genre = Genre.builder()
                .name(genreDto.getName())
                .description(genreDto.getDescription())
                .build();

        return modelMapper.map(genreRepository.save(genre), GenreDto.class);
    }

    @Override
    public GenreDto updateGenre(UUID id, GenreDto genreDto) {
        return null;
    }

    @Override
    public void deleteGenre(UUID id) {
        boolean dependentBooksExists = bookRepository.existsAllByGenresIn(List.of(genreRepository.findById(id).orElseThrow()));

        if (dependentBooksExists) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No se puede eliminar el género porque tiene libros asociados, elimina los libros primero");
        }

        genreRepository.deleteById(id);
    }
}
