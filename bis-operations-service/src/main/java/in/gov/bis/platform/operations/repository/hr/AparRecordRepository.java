package in.gov.bis.platform.operations.repository.hr;

import in.gov.bis.platform.operations.domain.hr.AparRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AparRecordRepository extends JpaRepository<AparRecord, Long> {
    List<AparRecord> findByEmployeeNo(String employeeNo);
    List<AparRecord> findByFinancialYear(String financialYear);
}
