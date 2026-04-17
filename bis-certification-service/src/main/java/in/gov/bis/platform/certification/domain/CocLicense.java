package in.gov.bis.platform.certification.domain;
import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Entity @Table(name = "coc_licenses")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CocLicense {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private UUID applicationId;
    @Column(unique = true) private String licenseNo;
    private String applicantName;
    private String productName;
    private String standardNo;
    private String scope;
    private String factoryAddress;
    private String status;
    private String osaAgency;
    private LocalDate validFrom;
    private LocalDate validTo;
    private Instant createdAt;
}
