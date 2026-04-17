package in.gov.bis.platform.certification.domain;
import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Entity @Table(name = "mscs_licenses")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class MscsLicense {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private UUID applicationId;
    @Column(unique = true) private String licenseNo;
    private String applicantName;
    private String managementSystem;
    private String scope;
    private String certificationBody;
    private String auditType;
    private LocalDate auditDate;
    private String auditorName;
    private LocalDate validFrom;
    private LocalDate validTo;
    private String status;
    private Instant createdAt;
}
