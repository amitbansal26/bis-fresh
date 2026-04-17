package in.gov.bis.platform.integration.domain;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.Instant;

@Entity @Table(name = "payment_gateway_transactions")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class PaymentGatewayTransaction {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true) private String transactionId;
    private String gateway;
    private String orderId;
    private BigDecimal amount;
    private String currency;
    private String status;
    private String gatewayRef;
    private String payerName;
    private String payerEmail;
    private Instant initiatedAt;
    private Instant completedAt;
    private Instant createdAt;
}
