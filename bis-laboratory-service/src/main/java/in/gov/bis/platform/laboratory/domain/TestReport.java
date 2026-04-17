package in.gov.bis.platform.laboratory.domain;
import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;

@Entity @Table(name = "test_reports")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class TestReport {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true) private String reportNo;
    private String sampleNo;
    private String labCode;
    private String licenseNo;
    private Instant issuedAt;
    private String issuedBy;
    private String conclusion;
    private String signedBy;
    private Instant createdAt;
}
