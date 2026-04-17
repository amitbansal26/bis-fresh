package in.gov.bis.platform.operations.domain.finance;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

@Entity
@Table(name = "finance_payments")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class FinancePayment {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true) private String voucherNo;
    private String paymentType;
    private String payeeName;
    private BigDecimal amount;
    private String currency;
    private LocalDate paymentDate;
    private String status;
    private String bankRef;
    private String approvedBy;
    private Instant createdAt;
}
