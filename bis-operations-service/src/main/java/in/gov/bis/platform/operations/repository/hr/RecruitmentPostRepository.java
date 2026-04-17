package in.gov.bis.platform.operations.repository.hr;

import in.gov.bis.platform.operations.domain.hr.RecruitmentPost;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface RecruitmentPostRepository extends JpaRepository<RecruitmentPost, Long> {
    List<RecruitmentPost> findByStatus(String status);
}
