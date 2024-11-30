package ec.edu.espe.kibook.repository;

import ec.edu.espe.kibook.entity.Author;
import org.springframework.data.repository.ListCrudRepository;

import java.util.Set;
import java.util.UUID;

public interface AuthorRepository extends ListCrudRepository<Author, UUID> {
    Set<Author> findAllByIdIn(Set<UUID> ids);
}
