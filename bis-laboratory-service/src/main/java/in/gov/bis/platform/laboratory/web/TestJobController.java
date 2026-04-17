package in.gov.bis.platform.laboratory.web;
import in.gov.bis.platform.laboratory.domain.TestJob;
import in.gov.bis.platform.laboratory.repository.TestJobRepository;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/labs/{labCode}/test-jobs")
public class TestJobController {
    private final TestJobRepository repo;
    public TestJobController(TestJobRepository repo) { this.repo = repo; }
    @GetMapping public List<TestJob> list(@PathVariable String labCode) { return repo.findByLabCode(labCode); }
    @PostMapping public TestJob create(@PathVariable String labCode, @RequestBody TestJob j) { j.setLabCode(labCode); j.setStatus("ASSIGNED"); return repo.save(j); }
    @GetMapping("/{id}") public TestJob get(@PathVariable String labCode, @PathVariable Long id) { return repo.findById(id).orElseThrow(); }
}
