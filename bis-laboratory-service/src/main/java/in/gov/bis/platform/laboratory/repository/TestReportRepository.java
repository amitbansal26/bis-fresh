package in.gov.bis.platform.laboratory.repository;
import in.gov.bis.platform.laboratory.domain.TestReport;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
public interface TestReportRepository extends JpaRepository<TestReport, Long> {
    List<TestReport> findByLabCode(String labCode);
    Optional<TestReport> findByReportNo(String reportNo);
}
