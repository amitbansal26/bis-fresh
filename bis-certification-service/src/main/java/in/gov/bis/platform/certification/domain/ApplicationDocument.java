package in.gov.bis.platform.certification.domain;
import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;
import java.util.UUID;

@Entity @Table(name = "application_documents")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ApplicationDocument {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private UUID applicationId;
    private String documentType;
    private String documentName;
    private String documentPath;
    private String uploadedBy;
    private Instant uploadedAt;
    private boolean verified;
}
