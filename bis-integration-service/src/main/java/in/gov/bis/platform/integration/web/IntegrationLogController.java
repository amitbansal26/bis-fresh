package in.gov.bis.platform.integration.web;

import in.gov.bis.platform.integration.domain.IntegrationLog;
import in.gov.bis.platform.integration.repository.IntegrationLogRepository;
import org.springframework.web.bind.annotation.*;
import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/api/integration/logs")
public class IntegrationLogController {
    private final IntegrationLogRepository repo;

    public IntegrationLogController(IntegrationLogRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public List<IntegrationLog> list(@RequestParam(required = false) String integrationName) {
        return integrationName != null ? repo.findByIntegrationName(integrationName) : repo.findAll();
    }

    @PostMapping
    public IntegrationLog log(@RequestBody IntegrationLog l) {
        l.setCreatedAt(Instant.now());
        return repo.save(l);
    }
}
