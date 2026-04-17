package in.gov.bis.platform.operations.repository.finance;

import in.gov.bis.platform.operations.domain.finance.FinancePayment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface FinancePaymentRepository extends JpaRepository<FinancePayment, Long> {
    List<FinancePayment> findByStatus(String status);
}
