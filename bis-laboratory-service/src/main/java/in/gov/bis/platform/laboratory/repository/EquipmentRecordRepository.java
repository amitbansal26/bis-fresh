package in.gov.bis.platform.laboratory.repository;
import in.gov.bis.platform.laboratory.domain.EquipmentRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface EquipmentRecordRepository extends JpaRepository<EquipmentRecord, Long> {
    List<EquipmentRecord> findByLabCode(String labCode);
}
