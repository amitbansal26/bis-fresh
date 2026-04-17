package in.gov.bis.platform.certification.repository;
import in.gov.bis.platform.certification.domain.CertificationApplication;
import in.gov.bis.platform.certification.domain.ApplicationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
public interface CertificationApplicationRepository extends JpaRepository<CertificationApplication, UUID> {
    List<CertificationApplication> findBySchemeCode(String schemeCode);
    List<CertificationApplication> findByStatus(ApplicationStatus status);
    Optional<CertificationApplication> findByLicenseNo(String licenseNo);
    Optional<CertificationApplication> findByApplicationNo(String applicationNo);
}
