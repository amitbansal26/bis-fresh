package in.gov.bis.platform.masterdata.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "product_categories")
@Getter
@Setter
@NoArgsConstructor
public class ProductCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String code;

    @Column(nullable = false)
    private String name;

    private String schemeCode;

    private Long parentCategoryId;

    @Column(nullable = false)
    private boolean active = true;
}
