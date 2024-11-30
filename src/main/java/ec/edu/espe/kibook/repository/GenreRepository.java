package ec.edu.espe.kibook.repository;

import ec.edu.espe.kibook.entity.Genre;
import org.springframework.data.repository.ListCrudRepository;

import java.util.Set;
import java.util.UUID;

public interface GenreRepository extends ListCrudRepository<Genre, UUID> {
    Set<Genre> findAllByIdIn(Set<UUID> ids);
}
