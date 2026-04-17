package in.gov.bis.platform.certification.repository;
import in.gov.bis.platform.certification.domain.WorkflowHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;
public interface WorkflowHistoryRepository extends JpaRepository<WorkflowHistory, Long> {
    List<WorkflowHistory> findByApplicationIdOrderByPerformedAtDesc(UUID applicationId);
}
