package in.gov.bis.platform.integration.web;

import in.gov.bis.platform.integration.domain.Notification;
import in.gov.bis.platform.integration.domain.NotificationTemplate;
import in.gov.bis.platform.integration.repository.NotificationRepository;
import in.gov.bis.platform.integration.repository.NotificationTemplateRepository;
import org.springframework.web.bind.annotation.*;
import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {
    private final NotificationRepository repo;
    private final NotificationTemplateRepository tplRepo;

    public NotificationController(NotificationRepository r, NotificationTemplateRepository t) {
        repo = r;
        tplRepo = t;
    }

    @GetMapping
    public List<Notification> list(@RequestParam(required = false) String status) {
        return status != null ? repo.findByStatus(status) : repo.findAll();
    }

    @PostMapping
    public Notification create(@RequestBody Notification n) {
        n.setStatus("PENDING");
        n.setCreatedAt(Instant.now());
        return repo.save(n);
    }

    @PostMapping("/{id}/send")
    public Notification send(@PathVariable Long id) {
        Notification n = repo.findById(id).orElseThrow();
        n.setStatus("SENT");
        n.setSentAt(Instant.now());
        return repo.save(n);
    }

    @GetMapping("/templates")
    public List<NotificationTemplate> listTemplates() {
        return tplRepo.findAll();
    }

    @PostMapping("/templates")
    public NotificationTemplate createTemplate(@RequestBody NotificationTemplate t) {
        t.setActive(true);
        t.setCreatedAt(Instant.now());
        return tplRepo.save(t);
    }
}
