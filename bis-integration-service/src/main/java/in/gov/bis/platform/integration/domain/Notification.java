package in.gov.bis.platform.integration.domain;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;

@Entity @Table(name = "notifications")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Notification {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String recipientId;
    private String recipientEmail;
    private String recipientMobile;
    private String notificationType;
    private String subject;
    @Column(length = 4000) private String body;
    private String status;
    private String referenceId;
    private String referenceType;
    private Instant scheduledAt;
    private Instant sentAt;
    private Instant createdAt;
}
