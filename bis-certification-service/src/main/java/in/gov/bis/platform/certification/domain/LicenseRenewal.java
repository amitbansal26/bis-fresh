package in.gov.bis.platform.certification.domain;
import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Entity @Table(name = "license_renewals")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class LicenseRenewal {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private UUID applicationId;
    private String licenseNo;
    private String renewalType;
    private LocalDate currentExpiry;
    private LocalDate newExpiry;
    private String status;
    private Instant appliedAt;
    private Instant processedAt;
    private String processedBy;
}
