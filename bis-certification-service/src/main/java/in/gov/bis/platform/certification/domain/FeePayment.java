package in.gov.bis.platform.certification.domain;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity @Table(name = "fee_payments")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class FeePayment {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private UUID applicationId;
    private String licenseNo;
    private String feeType;
    private BigDecimal amount;
    @Builder.Default private String currency = "INR";
    private String paymentMode;
    private String transactionRef;
    private String status;
    private Instant paidAt;
    private String receiptNo;
    private Instant createdAt;
}
