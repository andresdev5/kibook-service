package ec.edu.espe.kibook.controller;

import ec.edu.espe.kibook.dto.AuthorDto;
import ec.edu.espe.kibook.service.AuthorService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/authors")
@RequiredArgsConstructor
public class AuthorsController {
    private final AuthorService authorService;

    @GetMapping
    public List<AuthorDto> getAuthors() {
        return authorService.getAuthors();
    }

    @GetMapping("/{id}")
    public AuthorDto getAuthorById(@PathVariable UUID id) {
        return authorService.getAuthorById(id);
    }

    @PostMapping
    public AuthorDto createAuthor(@RequestBody @Validated({ AuthorDto.Create.class }) AuthorDto authorDto) {
        return authorService.createAuthor(authorDto);
    }

    @PutMapping("/{id}")
    public AuthorDto updateAuthor(@PathVariable UUID id, @RequestBody @Validated({ AuthorDto.Update.class }) AuthorDto authorDto) {
        return authorService.updateAuthor(id, authorDto);
    }

    @DeleteMapping("/{id}")
    public void deleteAuthor(@PathVariable UUID id) {
        authorService.deleteAuthor(id);
    }
}
