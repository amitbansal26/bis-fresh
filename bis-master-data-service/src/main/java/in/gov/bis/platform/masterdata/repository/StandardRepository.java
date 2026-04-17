package in.gov.bis.platform.masterdata.repository;

import in.gov.bis.platform.masterdata.domain.Standard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StandardRepository extends JpaRepository<Standard, Long> {
}
