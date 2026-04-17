package in.gov.bis.platform.certification.web;
import in.gov.bis.platform.certification.domain.CocLicense;
import in.gov.bis.platform.certification.repository.CocLicenseRepository;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/coc")
public class CocController {
    private final CocLicenseRepository repo;
    public CocController(CocLicenseRepository repo) { this.repo = repo; }
    @GetMapping public List<CocLicense> list() { return repo.findAll(); }
    @PostMapping public CocLicense create(@RequestBody CocLicense r) { return repo.save(r); }
    @GetMapping("/{id}") public CocLicense get(@PathVariable Long id) { return repo.findById(id).orElseThrow(); }
}
