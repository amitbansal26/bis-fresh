package in.gov.bis.platform.masterdata.repository;

import in.gov.bis.platform.masterdata.domain.Office;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OfficeRepository extends JpaRepository<Office, Long> {
}
