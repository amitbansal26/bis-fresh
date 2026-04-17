package in.gov.bis.platform.operations.domain.facilities;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;
import java.time.LocalDate;

@Entity
@Table(name = "library_books")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class LibraryBook {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true) private String accessionNo;
    private String title;
    private String author;
    private String isbn;
    private String category;
    private boolean available;
    private String issuedTo;
    private LocalDate issueDate;
    private LocalDate returnDate;
    private Instant createdAt;
}
