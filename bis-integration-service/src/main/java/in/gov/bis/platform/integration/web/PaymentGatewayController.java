package in.gov.bis.platform.integration.web;

import in.gov.bis.platform.integration.domain.PaymentGatewayTransaction;
import in.gov.bis.platform.integration.repository.PaymentGatewayTransactionRepository;
import org.springframework.web.bind.annotation.*;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/payment-gateway")
public class PaymentGatewayController {
    private final PaymentGatewayTransactionRepository repo;

    public PaymentGatewayController(PaymentGatewayTransactionRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public List<PaymentGatewayTransaction> list() {
        return repo.findAll();
    }

    @PostMapping("/initiate")
    public PaymentGatewayTransaction initiate(@RequestBody PaymentGatewayTransaction t) {
        t.setTransactionId("TXN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        t.setStatus("INITIATED");
        t.setInitiatedAt(Instant.now());
        t.setCreatedAt(Instant.now());
        return repo.save(t);
    }

    @PostMapping("/{id}/callback")
    public PaymentGatewayTransaction callback(@PathVariable Long id, @RequestParam String status) {
        PaymentGatewayTransaction t = repo.findById(id).orElseThrow();
        t.setStatus(status);
        t.setCompletedAt(Instant.now());
        return repo.save(t);
    }

    @GetMapping("/{transactionId}")
    public PaymentGatewayTransaction get(@PathVariable String transactionId) {
        return repo.findByTransactionId(transactionId).orElseThrow();
    }
}
