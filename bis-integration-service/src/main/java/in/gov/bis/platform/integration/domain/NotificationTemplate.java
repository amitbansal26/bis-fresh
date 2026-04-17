package in.gov.bis.platform.integration.domain;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;

@Entity @Table(name = "notification_templates")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class NotificationTemplate {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true) private String templateCode;
    private String templateName;
    private String channel;
    private String subject;
    @Column(length = 4000) private String bodyTemplate;
    private boolean active;
    private Instant createdAt;
}
