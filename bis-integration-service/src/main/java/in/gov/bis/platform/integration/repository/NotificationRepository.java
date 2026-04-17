package in.gov.bis.platform.integration.repository;

import in.gov.bis.platform.integration.domain.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByRecipientId(String recipientId);
    List<Notification> findByStatus(String status);
}
