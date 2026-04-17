package in.gov.bis.platform.laboratory.domain;
import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;

@Entity @Table(name = "lab_samples")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Sample {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true) private String sampleNo;
    private String licenseNo;
    private String labCode;
    private String sampleType;
    private String productName;
    private String productCategory;
    private String manufacturerName;
    private Instant receivedAt;
    private String receivedBy;
    private boolean sealIntact;
    private String quantity;
    private String status;
    private String reportNo;
    private Instant createdAt;
}
