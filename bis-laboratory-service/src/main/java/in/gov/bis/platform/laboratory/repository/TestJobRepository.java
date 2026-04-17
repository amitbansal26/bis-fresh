package in.gov.bis.platform.laboratory.repository;
import in.gov.bis.platform.laboratory.domain.TestJob;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface TestJobRepository extends JpaRepository<TestJob, Long> {
    List<TestJob> findByLabCode(String labCode);
}
