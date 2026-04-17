package in.gov.bis.platform.integration.repository;

import in.gov.bis.platform.integration.domain.NotificationTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface NotificationTemplateRepository extends JpaRepository<NotificationTemplate, Long> {
    Optional<NotificationTemplate> findByTemplateCode(String templateCode);
}
