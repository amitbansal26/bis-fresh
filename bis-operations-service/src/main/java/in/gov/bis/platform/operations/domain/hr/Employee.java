package in.gov.bis.platform.operations.domain.hr;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;
import java.time.LocalDate;

@Entity
@Table(name = "employees")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Employee {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true) private String employeeNo;
    private String name;
    private String designation;
    private String grade;
    private String department;
    private String officeCode;
    private String employeeType;
    private LocalDate dateOfJoining;
    private LocalDate dateOfBirth;
    private LocalDate dateOfRetirement;
    private String email;
    private String mobile;
    private String status;
    private Instant createdAt;
}
