package in.gov.bis.platform.laboratory.repository;
import in.gov.bis.platform.laboratory.domain.LrsAuditRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface LrsAuditRecordRepository extends JpaRepository<LrsAuditRecord, Long> {
    List<LrsAuditRecord> findByRecognitionNo(String recognitionNo);
}
