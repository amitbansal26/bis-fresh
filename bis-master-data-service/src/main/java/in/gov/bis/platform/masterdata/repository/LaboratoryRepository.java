package in.gov.bis.platform.masterdata.repository;

import in.gov.bis.platform.masterdata.domain.Laboratory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LaboratoryRepository extends JpaRepository<Laboratory, Long> {
}
