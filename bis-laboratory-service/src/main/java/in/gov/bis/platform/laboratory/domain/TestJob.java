package in.gov.bis.platform.laboratory.domain;
import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;

@Entity @Table(name = "test_jobs")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class TestJob {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String sampleNo;
    private String labCode;
    private String assignedScientist;
    private String standardNo;
    private String testMethod;
    private Instant startedAt;
    private Instant completedAt;
    private String testResult;
    private String observations;
    private String status;
}
