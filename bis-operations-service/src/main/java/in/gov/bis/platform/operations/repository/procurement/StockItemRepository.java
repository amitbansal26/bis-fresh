package in.gov.bis.platform.operations.repository.procurement;

import in.gov.bis.platform.operations.domain.procurement.StockItem;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface StockItemRepository extends JpaRepository<StockItem, Long> {
    List<StockItem> findByOfficeCode(String officeCode);
}
