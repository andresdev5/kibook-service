package ec.edu.espe.kibook.service;

import ec.edu.espe.kibook.dto.GenreDto;

import java.util.List;
import java.util.UUID;

public interface GenreService {
    List<GenreDto> getGenres();
    GenreDto getGenreById(UUID id);
    GenreDto createGenre(GenreDto genreDto);
    GenreDto updateGenre(UUID id, GenreDto genreDto);
    void deleteGenre(UUID id);
}
