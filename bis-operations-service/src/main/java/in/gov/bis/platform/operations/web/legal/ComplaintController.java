package in.gov.bis.platform.operations.web.legal;

import in.gov.bis.platform.operations.domain.legal.Complaint;
import in.gov.bis.platform.operations.repository.legal.ComplaintRepository;
import org.springframework.web.bind.annotation.*;
import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/api/legal/complaints")
public class ComplaintController {
    private final ComplaintRepository repo;
    public ComplaintController(ComplaintRepository repo) { this.repo = repo; }

    @GetMapping
    public List<Complaint> list() { return repo.findAll(); }

    @PostMapping
    public Complaint create(@RequestBody Complaint c) {
        c.setCreatedAt(Instant.now());
        return repo.save(c);
    }

    @GetMapping("/{id}")
    public Complaint get(@PathVariable Long id) {
        return repo.findById(id).orElseThrow();
    }
}
