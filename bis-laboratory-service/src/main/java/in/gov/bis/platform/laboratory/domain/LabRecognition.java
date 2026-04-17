package in.gov.bis.platform.laboratory.domain;
import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Entity @Table(name = "lab_recognitions")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class LabRecognition {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private UUID applicationId;
    @Column(unique = true) private String recognitionNo;
    private String labName;
    private String labAddress;
    private String state;
    private String disciplines;
    private String recognitionType;
    private String status;
    private LocalDate grantedAt;
    private LocalDate validUntil;
    private LocalDate lastAuditDate;
    private LocalDate nextAuditDate;
    private Instant createdAt;
}
