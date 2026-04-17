package in.gov.bis.platform.certification.domain;
import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Entity @Table(name = "hallmarking_ahcs")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class HallmarkingAhc {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private UUID applicationId;
    @Column(unique = true) private String recognitionNo;
    private String ahcName;
    private String address;
    private String state;
    private boolean karat14;
    private boolean karat18;
    private boolean karat22;
    private boolean karat24;
    private String status;
    private LocalDate recognizedAt;
    private LocalDate renewalDate;
    private Instant createdAt;
}
