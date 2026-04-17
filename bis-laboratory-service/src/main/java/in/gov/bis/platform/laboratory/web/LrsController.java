package in.gov.bis.platform.laboratory.web;
import in.gov.bis.platform.laboratory.domain.LabRecognition;
import in.gov.bis.platform.laboratory.domain.LrsAuditRecord;
import in.gov.bis.platform.laboratory.repository.LabRecognitionRepository;
import in.gov.bis.platform.laboratory.repository.LrsAuditRecordRepository;
import org.springframework.web.bind.annotation.*;
import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/api/lrs")
public class LrsController {
    private final LabRecognitionRepository recRepo;
    private final LrsAuditRecordRepository auditRepo;
    public LrsController(LabRecognitionRepository r, LrsAuditRecordRepository a) { recRepo=r; auditRepo=a; }
    @GetMapping public List<LabRecognition> list() { return recRepo.findAll(); }
    @PostMapping public LabRecognition create(@RequestBody LabRecognition r) { r.setCreatedAt(Instant.now()); return recRepo.save(r); }
    @GetMapping("/{id}") public LabRecognition get(@PathVariable Long id) { return recRepo.findById(id).orElseThrow(); }
    @PutMapping("/{id}") public LabRecognition update(@PathVariable Long id, @RequestBody LabRecognition r) { r.setId(id); return recRepo.save(r); }
    @GetMapping("/{recognitionNo}/audits") public List<LrsAuditRecord> audits(@PathVariable String recognitionNo) { return auditRepo.findByRecognitionNo(recognitionNo); }
    @PostMapping("/{recognitionNo}/audits") public LrsAuditRecord addAudit(@PathVariable String recognitionNo, @RequestBody LrsAuditRecord a) { a.setRecognitionNo(recognitionNo); a.setCreatedAt(Instant.now()); return auditRepo.save(a); }
}
