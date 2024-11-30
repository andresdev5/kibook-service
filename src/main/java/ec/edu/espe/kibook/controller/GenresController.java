package ec.edu.espe.kibook.controller;

import ec.edu.espe.kibook.dto.GenreDto;
import ec.edu.espe.kibook.service.GenreService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/genres")
@RequiredArgsConstructor
public class GenresController {
    private final GenreService genreService;

    @GetMapping
    public List<GenreDto> getGenres() {
        return genreService.getGenres();
    }

    @GetMapping("/{id}")
    public GenreDto getGenreById(@PathVariable UUID id) {
        return genreService.getGenreById(id);
    }

    @PostMapping
    public GenreDto createGenre(@RequestBody @Validated({ GenreDto.Create.class }) GenreDto genreDto) {
        return genreService.createGenre(genreDto);
    }

    @PutMapping("/{id}")
    public GenreDto updateGenre(@PathVariable UUID id, @RequestBody @Validated({ GenreDto.Update.class }) GenreDto genreDto) {
        return genreService.updateGenre(id, genreDto);
    }

    @DeleteMapping("/{id}")
    public void deleteGenre(@PathVariable UUID id) {
        genreService.deleteGenre(id);
    }
}
