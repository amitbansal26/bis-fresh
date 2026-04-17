package in.gov.bis.platform.certification.domain;
import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Entity @Table(name = "crs_registrations")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CrsRegistration {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private UUID applicationId;
    @Column(unique = true) private String registrationNo;
    private String modelNo;
    private String brandName;
    private String manufacturerName;
    private String manufacturerCountry;
    private String importerName;
    private String importerAddress;
    private String productCategory;
    private String standardNo;
    private String testReportNo;
    private String testLabName;
    private LocalDate validityDate;
    private String status;
    private String cancelReason;
    private boolean cclFlag;
    private Instant createdAt;
    private Instant updatedAt;
}
