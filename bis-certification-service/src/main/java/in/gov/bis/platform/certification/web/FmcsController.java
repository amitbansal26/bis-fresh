package in.gov.bis.platform.certification.web;
import in.gov.bis.platform.certification.domain.FmcsLicense;
import in.gov.bis.platform.certification.repository.FmcsLicenseRepository;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/fmcs")
public class FmcsController {
    private final FmcsLicenseRepository repo;
    public FmcsController(FmcsLicenseRepository repo) { this.repo = repo; }
    @GetMapping public List<FmcsLicense> list() { return repo.findAll(); }
    @PostMapping public FmcsLicense create(@RequestBody FmcsLicense r) { return repo.save(r); }
    @GetMapping("/{id}") public FmcsLicense get(@PathVariable Long id) { return repo.findById(id).orElseThrow(); }
}
