package ec.edu.espe.kibook.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthorDto {
    public interface Create {}
    public interface Update {}

    @NotEmpty(groups = {Update.class, BookDto.Create.class})
    private UUID id;

    @NotEmpty(groups = {Create.class, Update.class})
    private String firstName;

    @NotEmpty(groups = {Create.class, Update.class})
    private String lastName;

    @NotEmpty(groups = {Create.class, Update.class})
    private String pseudonym;

    @NotEmpty(groups = {Create.class, Update.class})
    private String biography;

    @NotNull(groups = {Create.class, Update.class})
    private LocalDate birthDate;
}
