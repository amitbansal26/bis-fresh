package in.gov.bis.platform.operations.web.finance;

import in.gov.bis.platform.operations.domain.finance.Receipt;
import in.gov.bis.platform.operations.repository.finance.ReceiptRepository;
import org.springframework.web.bind.annotation.*;
import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/api/finance/receipts")
public class ReceiptController {
    private final ReceiptRepository repo;
    public ReceiptController(ReceiptRepository repo) { this.repo = repo; }

    @GetMapping
    public List<Receipt> list() { return repo.findAll(); }

    @PostMapping
    public Receipt create(@RequestBody Receipt r) {
        r.setCreatedAt(Instant.now());
        return repo.save(r);
    }

    @GetMapping("/{id}")
    public Receipt get(@PathVariable Long id) {
        return repo.findById(id).orElseThrow();
    }
}
