package in.gov.bis.platform.laboratory.domain;
import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;
import java.time.LocalDate;

@Entity @Table(name = "equipment_records")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class EquipmentRecord {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String labCode;
    @Column(unique = true) private String equipmentNo;
    private String equipmentName;
    private String make;
    private String model;
    private String serialNo;
    private LocalDate calibrationDueDate;
    private LocalDate lastCalibrationDate;
    private String calibrationAgency;
    private String condition;
    private String location;
    private Instant createdAt;
}
