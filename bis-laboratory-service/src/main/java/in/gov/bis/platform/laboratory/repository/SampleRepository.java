package in.gov.bis.platform.laboratory.repository;
import in.gov.bis.platform.laboratory.domain.Sample;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
public interface SampleRepository extends JpaRepository<Sample, Long> {
    List<Sample> findByLabCode(String labCode);
    Optional<Sample> findBySampleNo(String sampleNo);
}
