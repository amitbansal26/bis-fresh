package in.gov.bis.platform.operations.domain.hr;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;
import java.time.LocalDate;

@Entity
@Table(name = "recruitment_posts")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class RecruitmentPost {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String postCode;
    private String postName;
    private String grade;
    private String department;
    private int sanctionedStrength;
    private int filledPosts;
    private int vacancies;
    private String recruitmentMode;
    private String advertisementNo;
    private LocalDate advertisementDate;
    private String status;
    private Instant createdAt;
}
