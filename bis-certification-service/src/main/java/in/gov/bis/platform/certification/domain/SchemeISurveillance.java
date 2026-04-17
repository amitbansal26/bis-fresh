package in.gov.bis.platform.certification.domain;
import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Entity @Table(name = "scheme_i_surveillances")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class SchemeISurveillance {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private UUID applicationId;
    private String licenseNo;
    private String surveillanceType;
    private LocalDate scheduledDate;
    private LocalDate conductedDate;
    private String inspector;
    private String outcome;
    private String findings;
    private LocalDate nextSurveillanceDate;
    private Instant createdAt;
}
