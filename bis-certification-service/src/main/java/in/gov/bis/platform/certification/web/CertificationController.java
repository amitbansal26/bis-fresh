package in.gov.bis.platform.certification.web;
import in.gov.bis.platform.certification.domain.CertificationApplication;
import in.gov.bis.platform.certification.domain.WorkflowHistory;
import in.gov.bis.platform.certification.service.CertificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/certifications")
public class CertificationController {
    private final CertificationService service;
    public CertificationController(CertificationService service) { this.service = service; }

    @PostMapping
    public ResponseEntity<CertificationApplication> submit(@RequestBody Map<String,String> body) {
        return ResponseEntity.ok(service.submit(
            body.get("schemeCode"), body.get("applicantName"), body.get("companyName"),
            body.get("productName"), body.get("productCategory"), body.get("standardNo"),
            body.get("factoryAddress"), body.get("factoryState"), body.get("factoryPinCode")));
    }

    @GetMapping
    public List<CertificationApplication> list(@RequestParam(required=false) String schemeCode) {
        return service.listByScheme(schemeCode);
    }

    @GetMapping("/{id}")
    public CertificationApplication get(@PathVariable UUID id) { return service.getById(id); }

    @GetMapping("/{id}/history")
    public List<WorkflowHistory> history(@PathVariable UUID id) { return service.getHistory(id); }
}
