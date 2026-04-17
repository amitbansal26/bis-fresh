package in.gov.bis.platform.integration.domain;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;

@Entity @Table(name = "integration_logs")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class IntegrationLog {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String integrationName;
    private String endpoint;
    private String requestPayload;
    private String responsePayload;
    private int httpStatus;
    private String status;
    private long durationMs;
    private String correlationId;
    private Instant createdAt;
}
