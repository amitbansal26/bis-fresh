package in.gov.bis.platform.operations.web.procurement;

import in.gov.bis.platform.operations.domain.procurement.PurchaseOrder;
import in.gov.bis.platform.operations.repository.procurement.PurchaseOrderRepository;
import org.springframework.web.bind.annotation.*;
import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/api/procurement/purchase-orders")
public class PurchaseOrderController {
    private final PurchaseOrderRepository repo;
    public PurchaseOrderController(PurchaseOrderRepository repo) { this.repo = repo; }

    @GetMapping
    public List<PurchaseOrder> list() { return repo.findAll(); }

    @PostMapping
    public PurchaseOrder create(@RequestBody PurchaseOrder p) {
        p.setCreatedAt(Instant.now());
        return repo.save(p);
    }

    @GetMapping("/{id}")
    public PurchaseOrder get(@PathVariable Long id) {
        return repo.findById(id).orElseThrow();
    }

    @PutMapping("/{id}")
    public PurchaseOrder update(@PathVariable Long id, @RequestBody PurchaseOrder p) {
        p.setId(id);
        return repo.save(p);
    }
}
