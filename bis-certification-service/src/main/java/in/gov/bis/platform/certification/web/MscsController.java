package in.gov.bis.platform.certification.web;
import in.gov.bis.platform.certification.domain.MscsLicense;
import in.gov.bis.platform.certification.repository.MscsLicenseRepository;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/mscs")
public class MscsController {
    private final MscsLicenseRepository repo;
    public MscsController(MscsLicenseRepository repo) { this.repo = repo; }
    @GetMapping public List<MscsLicense> list() { return repo.findAll(); }
    @PostMapping public MscsLicense create(@RequestBody MscsLicense r) { return repo.save(r); }
    @GetMapping("/{id}") public MscsLicense get(@PathVariable Long id) { return repo.findById(id).orElseThrow(); }
}
