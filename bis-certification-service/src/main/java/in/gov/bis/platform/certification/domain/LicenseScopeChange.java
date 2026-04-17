package in.gov.bis.platform.certification.domain;
import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;
import java.util.UUID;

@Entity @Table(name = "license_scope_changes")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class LicenseScopeChange {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private UUID applicationId;
    private String licenseNo;
    private String changeType;
    private String productsList;
    private String requestedBy;
    private Instant requestedAt;
    private String status;
    private String approvedBy;
    private Instant approvedAt;
}
