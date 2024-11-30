package ec.edu.espe.kibook.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookSearchParamsDto {
    public enum Order {
        ASC, DESC
    }

    @Getter
    @AllArgsConstructor
    public enum SortField {
        TITLE("title"),
        CREATED_AT("createdAt");

        private final String value;
    }

    private String title;
    private int page = 0;
    private int size = 10;
    private Order order = Order.DESC;
    private SortField sortField = SortField.CREATED_AT;
}
