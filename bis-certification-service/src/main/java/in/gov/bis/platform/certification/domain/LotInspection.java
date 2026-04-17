package in.gov.bis.platform.certification.domain;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity @Table(name = "lot_inspections")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class LotInspection {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String licenseNo;
    @Column(unique = true) private String lotNo;
    private String productCategory;
    private Integer sampleSize;
    private LocalDate inspectionDate;
    private String inspector;
    private String result;
    private String findings;
    private String marketplaceLocation;
}
