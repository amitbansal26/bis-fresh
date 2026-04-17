package in.gov.bis.platform.certification.domain;
import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Entity @Table(name = "otr_licenses")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class OtrLicense {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private UUID applicationId;
    @Column(unique = true) private String licenseNo;
    private String applicantName;
    private String productName;
    private String equipmentCategory;
    private String qcoNotification;
    private String standardNo;
    private String testReportNo;
    private String testLabName;
    private LocalDate validFrom;
    private LocalDate validTo;
    private String status;
    private Instant createdAt;
}
