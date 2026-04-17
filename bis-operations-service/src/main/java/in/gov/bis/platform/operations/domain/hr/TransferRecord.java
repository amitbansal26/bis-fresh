package in.gov.bis.platform.operations.domain.hr;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;
import java.time.LocalDate;

@Entity
@Table(name = "transfer_records")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class TransferRecord {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String employeeNo;
    private String fromOffice;
    private String toOffice;
    private String transferType;
    private LocalDate transferDate;
    private LocalDate joiningDate;
    private String orderNo;
    private String status;
    private Instant createdAt;
}
