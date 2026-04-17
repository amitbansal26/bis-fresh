package in.gov.bis.platform.operations.domain.finance;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

@Entity
@Table(name = "receipts")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Receipt {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true) private String receiptNo;
    private String receiptType;
    private String payerName;
    private BigDecimal amount;
    private LocalDate receiptDate;
    private String paymentMode;
    private String transactionRef;
    private String status;
    private Instant createdAt;
}
