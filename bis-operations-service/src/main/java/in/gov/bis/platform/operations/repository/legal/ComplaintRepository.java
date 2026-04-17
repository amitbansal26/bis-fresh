package in.gov.bis.platform.operations.repository.legal;

import in.gov.bis.platform.operations.domain.legal.Complaint;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ComplaintRepository extends JpaRepository<Complaint, Long> {
    List<Complaint> findByStatus(String status);
    List<Complaint> findByLicenseNo(String licenseNo);
}
