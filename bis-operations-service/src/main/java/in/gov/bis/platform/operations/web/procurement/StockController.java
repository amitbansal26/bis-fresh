package in.gov.bis.platform.operations.web.procurement;

import in.gov.bis.platform.operations.domain.procurement.StockItem;
import in.gov.bis.platform.operations.repository.procurement.StockItemRepository;
import org.springframework.web.bind.annotation.*;
import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/api/procurement/stock")
public class StockController {
    private final StockItemRepository repo;
    public StockController(StockItemRepository repo) { this.repo = repo; }

    @GetMapping
    public List<StockItem> list(@RequestParam(required = false) String officeCode) {
        return officeCode != null ? repo.findByOfficeCode(officeCode) : repo.findAll();
    }

    @PostMapping
    public StockItem create(@RequestBody StockItem s) {
        s.setCreatedAt(Instant.now());
        return repo.save(s);
    }

    @PutMapping("/{id}")
    public StockItem update(@PathVariable Long id, @RequestBody StockItem s) {
        s.setId(id);
        return repo.save(s);
    }
}
