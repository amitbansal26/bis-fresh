package in.gov.bis.platform.laboratory.web;
import in.gov.bis.platform.laboratory.domain.TestReport;
import in.gov.bis.platform.laboratory.repository.TestReportRepository;
import org.springframework.web.bind.annotation.*;
import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/api/labs/{labCode}/reports")
public class TestReportController {
    private final TestReportRepository repo;
    public TestReportController(TestReportRepository repo) { this.repo = repo; }
    @GetMapping public List<TestReport> list(@PathVariable String labCode) { return repo.findByLabCode(labCode); }
    @PostMapping public TestReport create(@PathVariable String labCode, @RequestBody TestReport r) { r.setLabCode(labCode); r.setIssuedAt(Instant.now()); r.setCreatedAt(Instant.now()); return repo.save(r); }
    @GetMapping("/{reportNo}") public TestReport get(@PathVariable String labCode, @PathVariable String reportNo) { return repo.findByReportNo(reportNo).orElseThrow(); }
}
