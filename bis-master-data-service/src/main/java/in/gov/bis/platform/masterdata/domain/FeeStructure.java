package in.gov.bis.platform.masterdata.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "fee_structures")
@Getter
@Setter
@NoArgsConstructor
public class FeeStructure {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String schemeCode;
    private String feeType;

    @Column(precision = 15, scale = 2)
    private BigDecimal amount;

    @Column(nullable = false)
    private String currency = "INR";

    private LocalDate effectiveDate;

    @Column(nullable = false)
    private boolean active = true;
}
