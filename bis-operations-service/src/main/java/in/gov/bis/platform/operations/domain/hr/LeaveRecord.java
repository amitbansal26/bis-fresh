package in.gov.bis.platform.operations.domain.hr;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;
import java.time.LocalDate;

@Entity
@Table(name = "leave_records")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class LeaveRecord {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String employeeNo;
    private String leaveType;
    private LocalDate fromDate;
    private LocalDate toDate;
    private int days;
    private String reason;
    private String status;
    private String approvedBy;
    private Instant appliedAt;
    private Instant processedAt;
}
