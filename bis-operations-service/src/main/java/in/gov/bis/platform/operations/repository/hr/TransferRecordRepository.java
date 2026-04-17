package in.gov.bis.platform.operations.repository.hr;

import in.gov.bis.platform.operations.domain.hr.TransferRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TransferRecordRepository extends JpaRepository<TransferRecord, Long> {
    List<TransferRecord> findByEmployeeNo(String employeeNo);
}
