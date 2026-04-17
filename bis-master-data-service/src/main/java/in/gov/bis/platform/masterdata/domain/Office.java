package in.gov.bis.platform.masterdata.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "offices")
@Getter
@Setter
@NoArgsConstructor
public class Office {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String officeCode;

    @Column(nullable = false)
    private String officeName;

    /** HQ / RO / BO / NITS / LAB */
    private String officeType;

    private String state;
    private String city;
    private String address;
    private String pinCode;

    @Column(nullable = false)
    private boolean active = true;
}
