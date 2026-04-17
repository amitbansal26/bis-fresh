package in.gov.bis.platform.operations.repository.hr;

import in.gov.bis.platform.operations.domain.hr.LeaveRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface LeaveRecordRepository extends JpaRepository<LeaveRecord, Long> {
    List<LeaveRecord> findByEmployeeNo(String employeeNo);
    List<LeaveRecord> findByStatus(String status);
}
