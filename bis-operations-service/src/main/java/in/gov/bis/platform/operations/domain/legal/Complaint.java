package in.gov.bis.platform.operations.domain.legal;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;

@Entity
@Table(name = "complaints")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Complaint {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true) private String complaintNo;
    private String complaintType;
    private String complainantName;
    private String complainantContact;
    private String productName;
    private String licenseNo;
    private String description;
    private String status;
    private String assignedTo;
    private String resolution;
    private Instant createdAt;
    private Instant resolvedAt;
}
