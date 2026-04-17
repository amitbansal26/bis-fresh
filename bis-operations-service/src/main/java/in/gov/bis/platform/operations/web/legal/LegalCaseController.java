package in.gov.bis.platform.operations.web.legal;

import in.gov.bis.platform.operations.domain.legal.LegalCase;
import in.gov.bis.platform.operations.repository.legal.LegalCaseRepository;
import org.springframework.web.bind.annotation.*;
import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/api/legal/cases")
public class LegalCaseController {
    private final LegalCaseRepository repo;
    public LegalCaseController(LegalCaseRepository repo) { this.repo = repo; }

    @GetMapping
    public List<LegalCase> list(@RequestParam(required = false) String status) {
        return status != null ? repo.findByStatus(status) : repo.findAll();
    }

    @PostMapping
    public LegalCase create(@RequestBody LegalCase c) {
        c.setCreatedAt(Instant.now());
        return repo.save(c);
    }

    @GetMapping("/{id}")
    public LegalCase get(@PathVariable Long id) {
        return repo.findById(id).orElseThrow();
    }

    @PutMapping("/{id}")
    public LegalCase update(@PathVariable Long id, @RequestBody LegalCase c) {
        c.setId(id);
        return repo.save(c);
    }
}
