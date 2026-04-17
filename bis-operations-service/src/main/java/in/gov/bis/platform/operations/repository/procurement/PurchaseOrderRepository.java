package in.gov.bis.platform.operations.repository.procurement;

import in.gov.bis.platform.operations.domain.procurement.PurchaseOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, Long> {
    List<PurchaseOrder> findByStatus(String status);
}
