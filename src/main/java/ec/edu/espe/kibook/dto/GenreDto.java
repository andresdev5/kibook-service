package ec.edu.espe.kibook.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GenreDto {
    interface Create {}
    interface Update {}

    @NotNull(groups = {Update.class, BookDto.Create.class})
    private UUID id;

    @NotNull(groups = {Create.class, Update.class})
    private String name;

    @NotNull(groups = {Create.class, Update.class})
    private String description;
}
