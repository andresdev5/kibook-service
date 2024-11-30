package ec.edu.espe.kibook.specification;

import ec.edu.espe.kibook.entity.Book;
import org.springframework.data.jpa.domain.Specification;

public class BookSpecification {
    private BookSpecification() {}

    public static Specification<Book> titleContains(String title) {
        return (root, query, criteriaBuilder) -> criteriaBuilder
                .like(criteriaBuilder.trim(criteriaBuilder.upper(root.get("title"))), "%" + title.trim().toUpperCase() + "%");
    }
}
