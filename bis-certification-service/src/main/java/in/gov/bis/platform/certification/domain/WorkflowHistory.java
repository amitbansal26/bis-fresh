package in.gov.bis.platform.certification.domain;
import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;
import java.util.UUID;

@Entity @Table(name = "workflow_history")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class WorkflowHistory {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private UUID applicationId;
    private String fromStatus;
    private String toStatus;
    private String action;
    private String performedBy;
    private Instant performedAt;
    private String comments;
}
