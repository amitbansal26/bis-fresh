package in.gov.bis.platform.certification.web;
import in.gov.bis.platform.certification.domain.OtrLicense;
import in.gov.bis.platform.certification.repository.OtrLicenseRepository;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/otr")
public class OtrController {
    private final OtrLicenseRepository repo;
    public OtrController(OtrLicenseRepository repo) { this.repo = repo; }
    @GetMapping public List<OtrLicense> list() { return repo.findAll(); }
    @PostMapping public OtrLicense create(@RequestBody OtrLicense r) { return repo.save(r); }
    @GetMapping("/{id}") public OtrLicense get(@PathVariable Long id) { return repo.findById(id).orElseThrow(); }
}
