package in.gov.bis.platform.operations.domain.hr;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;

@Entity
@Table(name = "apar_records")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class AparRecord {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String employeeNo;
    private String financialYear;
    private String reportingOfficer;
    private String reviewingOfficer;
    private String acceptingOfficer;
    private Double selfAppraisalScore;
    private Double reportingOfficerScore;
    private Double reviewingOfficerScore;
    private Double finalScore;
    private String grade;
    private String status;
    private Instant createdAt;
}
