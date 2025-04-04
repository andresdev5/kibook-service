package ec.edu.espe.kibook.repository;

import ec.edu.espe.kibook.entity.Author;
import ec.edu.espe.kibook.entity.Book;
import ec.edu.espe.kibook.entity.Genre;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;
import java.util.List;
import java.util.UUID;

@Repository
public interface BookRepository extends JpaRepository<Book, UUID>, JpaSpecificationExecutor<Book> {
    boolean existsAllByGenresIn(List<Genre> genres);
    boolean existsAllByAuthorsIn(List<Author> authors);
}
