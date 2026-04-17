package in.gov.bis.platform.certification.web;
import in.gov.bis.platform.certification.domain.CrsRegistration;
import in.gov.bis.platform.certification.repository.CrsRegistrationRepository;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/crs")
public class CrsController {
    private final CrsRegistrationRepository repo;
    public CrsController(CrsRegistrationRepository repo) { this.repo = repo; }
    @GetMapping public List<CrsRegistration> list() { return repo.findAll(); }
    @PostMapping public CrsRegistration create(@RequestBody CrsRegistration r) { return repo.save(r); }
    @GetMapping("/{id}") public CrsRegistration get(@PathVariable Long id) { return repo.findById(id).orElseThrow(); }
    @PutMapping("/{id}") public CrsRegistration update(@PathVariable Long id, @RequestBody CrsRegistration r) { r.setId(id); return repo.save(r); }
}
