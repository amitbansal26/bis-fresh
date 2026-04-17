package in.gov.bis.platform.certification.domain;
import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Entity @Table(name = "fmcs_licenses")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class FmcsLicense {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private UUID applicationId;
    @Column(unique = true) private String licenseNo;
    private String manufacturerName;
    private String manufacturerCountry;
    private String manufacturerAddress;
    private String productName;
    private String standardNo;
    private String indianAgentName;
    private String osaAgency;
    private String status;
    private LocalDate validFrom;
    private LocalDate validTo;
    private Instant createdAt;
}
