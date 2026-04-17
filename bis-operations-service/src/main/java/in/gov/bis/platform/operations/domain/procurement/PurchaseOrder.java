package in.gov.bis.platform.operations.domain.procurement;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

@Entity
@Table(name = "purchase_orders")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class PurchaseOrder {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true) private String poNo;
    private String vendorName;
    private String vendorCode;
    private String itemDescription;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal totalAmount;
    private LocalDate orderDate;
    private LocalDate deliveryDate;
    private String status;
    private String approvedBy;
    private Instant createdAt;
}
