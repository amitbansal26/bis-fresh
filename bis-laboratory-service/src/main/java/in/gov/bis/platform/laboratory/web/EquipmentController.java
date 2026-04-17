package in.gov.bis.platform.laboratory.web;
import in.gov.bis.platform.laboratory.domain.EquipmentRecord;
import in.gov.bis.platform.laboratory.repository.EquipmentRecordRepository;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/labs/{labCode}/equipment")
public class EquipmentController {
    private final EquipmentRecordRepository repo;
    public EquipmentController(EquipmentRecordRepository repo) { this.repo = repo; }
    @GetMapping public List<EquipmentRecord> list(@PathVariable String labCode) { return repo.findByLabCode(labCode); }
    @PostMapping public EquipmentRecord create(@PathVariable String labCode, @RequestBody EquipmentRecord e) { e.setLabCode(labCode); return repo.save(e); }
    @PutMapping("/{id}") public EquipmentRecord update(@PathVariable String labCode, @PathVariable Long id, @RequestBody EquipmentRecord e) { e.setId(id); e.setLabCode(labCode); return repo.save(e); }
}
