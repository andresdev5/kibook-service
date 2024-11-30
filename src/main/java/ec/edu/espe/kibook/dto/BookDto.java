package ec.edu.espe.kibook.dto;

import ec.edu.espe.kibook.entity.BookStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookDto {
    public interface Create {}
    public interface Update {}

    @NotNull(groups = {Update.class})
    private UUID id;

    @NotEmpty(groups = {Create.class, Update.class})
    private String title;

    @NotEmpty(groups = {Create.class, Update.class})
    @Pattern(regexp = "^[0-9]{3}-[0-9]-[0-9]{5}-[0-9]{3}-[0-9]$", groups = {Create.class, Update.class})
    private String isbn;

    private MultipartFile image;

    @Size(max = 500)
    @NotEmpty(groups = {Create.class, Update.class})
    private String synopsis;

    @NotEmpty(groups = {Create.class, Update.class})
    private String publisher;

    @Min(value = 0, groups = {Create.class, Update.class})
    private int year;

    @Enumerated(EnumType.STRING)
    @NotEmpty(groups = {Update.class})
    @Builder.Default
    private BookStatus status = BookStatus.AVAILABLE;

    @NotEmpty(groups = {Create.class, Update.class})
    @Valid
    private List<AuthorDto> authors;

    @NotEmpty(groups = {Create.class, Update.class})
    @Valid
    private List<GenreDto> genres;
}
