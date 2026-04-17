package in.gov.bis.platform.laboratory.domain;
import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;
import java.time.LocalDate;

@Entity @Table(name = "lrs_audit_records")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class LrsAuditRecord {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String recognitionNo;
    private String auditType;
    private LocalDate scheduledDate;
    private LocalDate conductedDate;
    private String outcome;
    private String findings;
    private Instant createdAt;
}
