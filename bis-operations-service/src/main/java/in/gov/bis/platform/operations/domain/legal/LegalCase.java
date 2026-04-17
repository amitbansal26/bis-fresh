package in.gov.bis.platform.operations.domain.legal;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;
import java.time.LocalDate;

@Entity
@Table(name = "legal_cases")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class LegalCase {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true) private String caseNo;
    private String caseType;
    private String courtName;
    private String petitioner;
    private String respondent;
    private LocalDate filingDate;
    private LocalDate nextHearingDate;
    private String status;
    private String assignedAdvocate;
    private String outcome;
    private Instant createdAt;
}
