package in.gov.bis.platform.laboratory.web;
import in.gov.bis.platform.laboratory.domain.Sample;
import in.gov.bis.platform.laboratory.repository.SampleRepository;
import org.springframework.web.bind.annotation.*;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/api/labs/{labCode}/samples")
public class SampleController {
    private static final AtomicLong SEQ = new AtomicLong(1000);
    private final SampleRepository repo;
    public SampleController(SampleRepository repo) { this.repo = repo; }
    @GetMapping public List<Sample> list(@PathVariable String labCode) { return repo.findByLabCode(labCode); }
    @PostMapping public Sample create(@PathVariable String labCode, @RequestBody Sample s) {
        s.setLabCode(labCode);
        s.setSampleNo("LAB-" + LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE) + "-" + SEQ.incrementAndGet());
        s.setStatus("RECEIVED");
        s.setReceivedAt(Instant.now());
        s.setCreatedAt(Instant.now());
        return repo.save(s);
    }
    @GetMapping("/{sampleNo}") public Sample get(@PathVariable String labCode, @PathVariable String sampleNo) {
        return repo.findBySampleNo(sampleNo).orElseThrow();
    }
}
