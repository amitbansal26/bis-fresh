package in.gov.bis.platform.operations.web.finance;

import in.gov.bis.platform.operations.domain.finance.FinancePayment;
import in.gov.bis.platform.operations.repository.finance.FinancePaymentRepository;
import org.springframework.web.bind.annotation.*;
import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/api/finance/payments")
public class PaymentController {
    private final FinancePaymentRepository repo;
    public PaymentController(FinancePaymentRepository repo) { this.repo = repo; }

    @GetMapping
    public List<FinancePayment> list() { return repo.findAll(); }

    @PostMapping
    public FinancePayment create(@RequestBody FinancePayment p) {
        p.setCreatedAt(Instant.now());
        return repo.save(p);
    }

    @GetMapping("/{id}")
    public FinancePayment get(@PathVariable Long id) {
        return repo.findById(id).orElseThrow();
    }
}
