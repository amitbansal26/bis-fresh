package in.gov.bis.platform.certification.repository;
import in.gov.bis.platform.certification.domain.FmcsLicense;
import org.springframework.data.jpa.repository.JpaRepository;
public interface FmcsLicenseRepository extends JpaRepository<FmcsLicense, Long> {}
