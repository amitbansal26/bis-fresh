package in.gov.bis.platform.operations.repository.legal;

import in.gov.bis.platform.operations.domain.legal.LegalCase;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface LegalCaseRepository extends JpaRepository<LegalCase, Long> {
    List<LegalCase> findByStatus(String status);
}
