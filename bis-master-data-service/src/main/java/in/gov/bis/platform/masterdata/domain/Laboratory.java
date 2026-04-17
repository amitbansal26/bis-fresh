package in.gov.bis.platform.masterdata.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "laboratories")
@Getter
@Setter
@NoArgsConstructor
public class Laboratory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String labCode;

    @Column(nullable = false)
    private String labName;

    /** INTERNAL / ACCREDITED / RECOGNIZED */
    private String labType;

    private String officeCode;
    private String state;
    private String city;
    private boolean nabl;

    /** Comma-separated discipline list */
    private String disciplines;

    @Column(nullable = false)
    private boolean active = true;
}
