package in.gov.bis.platform.masterdata.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Entity
@Table(name = "standards")
@Getter
@Setter
@NoArgsConstructor
public class Standard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String standardNo;

    @Column(nullable = false)
    private String title;

    private String version;

    /** CURRENT / WITHDRAWN / SUPERSEDED */
    private String status;

    private LocalDate publishedDate;
    private String applicableScheme;
}
