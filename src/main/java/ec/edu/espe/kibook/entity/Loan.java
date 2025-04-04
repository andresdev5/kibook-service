package ec.edu.espe.kibook.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "loans")
@EntityListeners(AuditingEntityListener.class)
public class Loan {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    private User borrower;

    @ManyToOne
    private Book book;

    @Enumerated(EnumType.STRING)
    private LoanStatus status;

    private LocalDate dueDate;
    private LocalDateTime loanedAt;
    private LocalDateTime returnedAt;

    @CreatedDate
    private LocalDateTime createdAt;

    @CreatedDate
    private LocalDateTime updatedAt;
}
