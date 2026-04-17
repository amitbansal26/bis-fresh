package in.gov.bis.platform.certification.domain;
import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Entity @Table(name = "hallmarking_jewellers")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class HallmarkingJeweller {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private UUID applicationId;
    @Column(unique = true) private String registrationNo;
    private String jewelerName;
    private String businessType;
    private String gstNo;
    private String primaryOutletAddress;
    private String primaryOutletState;
    private String status;
    private int outletsCount;
    private LocalDate registeredAt;
    private LocalDate renewalDate;
    private Instant createdAt;
}
