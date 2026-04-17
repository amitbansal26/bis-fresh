package in.gov.bis.platform.laboratory.repository;
import in.gov.bis.platform.laboratory.domain.LabRecognition;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface LabRecognitionRepository extends JpaRepository<LabRecognition, Long> {
    List<LabRecognition> findByStatus(String status);
}
