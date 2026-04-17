package in.gov.bis.platform.operations.domain.procurement;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "stock_items")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class StockItem {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true) private String itemCode;
    private String itemName;
    private String category;
    private BigDecimal quantityInStock;
    private String unit;
    private BigDecimal minimumStock;
    private String officeCode;
    private Instant lastUpdated;
    private Instant createdAt;
}
