package in.gov.bis.platform.integration.repository;

import in.gov.bis.platform.integration.domain.PaymentGatewayTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface PaymentGatewayTransactionRepository extends JpaRepository<PaymentGatewayTransaction, Long> {
    Optional<PaymentGatewayTransaction> findByTransactionId(String transactionId);
    List<PaymentGatewayTransaction> findByStatus(String status);
}
