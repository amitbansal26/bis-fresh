package in.gov.bis.platform.operations.web.hr;

import in.gov.bis.platform.operations.domain.hr.AparRecord;
import in.gov.bis.platform.operations.repository.hr.AparRecordRepository;
import org.springframework.web.bind.annotation.*;
import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/api/hr/apar")
public class AparController {
    private final AparRecordRepository repo;
    public AparController(AparRecordRepository repo) { this.repo = repo; }

    @GetMapping
    public List<AparRecord> list(@RequestParam(required = false) String employeeNo) {
        return employeeNo != null ? repo.findByEmployeeNo(employeeNo) : repo.findAll();
    }

    @PostMapping
    public AparRecord create(@RequestBody AparRecord a) {
        a.setCreatedAt(Instant.now());
        return repo.save(a);
    }

    @PutMapping("/{id}")
    public AparRecord update(@PathVariable Long id, @RequestBody AparRecord a) {
        a.setId(id);
        return repo.save(a);
    }
}
