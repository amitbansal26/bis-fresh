package in.gov.bis.platform.certification.domain;
import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Entity @Table(name = "certification_applications")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CertificationApplication {
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(unique = true, nullable = false) private String applicationNo;
    @Column(nullable = false) private String schemeCode;
    private UUID applicantId;
    private String applicantName;
    private String companyName;
    private String productName;
    private String productCategory;
    private String standardNo;
    private String factoryAddress;
    private String factoryState;
    private String factoryPinCode;
    @Enumerated(EnumType.STRING) private ApplicationStatus status;
    private Instant submittedAt;
    private String assignedOfficer;
    private String inspectorName;
    private Instant grantedAt;
    private String licenseNo;
    private LocalDate licenseExpiryDate;
    private String rejectionReason;
    private String remarks;
    private Instant createdAt;
    private Instant updatedAt;
}
