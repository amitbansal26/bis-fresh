package in.gov.bis.platform.masterdata.repository;

import in.gov.bis.platform.masterdata.domain.Scheme;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SchemeRepository extends JpaRepository<Scheme, Long> {
}
