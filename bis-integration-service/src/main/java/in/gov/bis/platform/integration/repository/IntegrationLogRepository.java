package in.gov.bis.platform.integration.repository;

import in.gov.bis.platform.integration.domain.IntegrationLog;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface IntegrationLogRepository extends JpaRepository<IntegrationLog, Long> {
    List<IntegrationLog> findByIntegrationName(String integrationName);
    List<IntegrationLog> findByStatus(String status);
}
