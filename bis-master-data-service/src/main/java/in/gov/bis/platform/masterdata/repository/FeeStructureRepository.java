package in.gov.bis.platform.masterdata.repository;

import in.gov.bis.platform.masterdata.domain.FeeStructure;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeeStructureRepository extends JpaRepository<FeeStructure, Long> {
}
