package in.gov.bis.platform.operations.repository.finance;

import in.gov.bis.platform.operations.domain.finance.Receipt;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ReceiptRepository extends JpaRepository<Receipt, Long> {
    List<Receipt> findByStatus(String status);
}
