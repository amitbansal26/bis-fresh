package in.gov.bis.platform.operations.web.hr;

import in.gov.bis.platform.operations.domain.hr.LeaveRecord;
import in.gov.bis.platform.operations.repository.hr.LeaveRecordRepository;
import org.springframework.web.bind.annotation.*;
import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/api/hr/leaves")
public class LeaveController {
    private final LeaveRecordRepository repo;
    public LeaveController(LeaveRecordRepository repo) { this.repo = repo; }

    @GetMapping
    public List<LeaveRecord> list(@RequestParam(required = false) String employeeNo) {
        return employeeNo != null ? repo.findByEmployeeNo(employeeNo) : repo.findAll();
    }

    @PostMapping
    public LeaveRecord apply(@RequestBody LeaveRecord l) {
        l.setStatus("PENDING");
        l.setAppliedAt(Instant.now());
        return repo.save(l);
    }

    @PutMapping("/{id}/approve")
    public LeaveRecord approve(@PathVariable Long id, @RequestParam String approvedBy) {
        LeaveRecord l = repo.findById(id).orElseThrow();
        l.setStatus("APPROVED");
        l.setApprovedBy(approvedBy);
        l.setProcessedAt(Instant.now());
        return repo.save(l);
    }
}
