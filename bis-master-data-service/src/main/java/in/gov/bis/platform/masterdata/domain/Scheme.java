package in.gov.bis.platform.masterdata.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "schemes")
@Getter
@Setter
@NoArgsConstructor
public class Scheme {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String code;

    @Column(nullable = false)
    private String name;

    private String schemeType;

    @Column(nullable = false)
    private boolean active = true;

    private String description;
}
