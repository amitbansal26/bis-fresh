package in.gov.bis.platform.operations.web.facilities;

import in.gov.bis.platform.operations.domain.facilities.RoomBooking;
import in.gov.bis.platform.operations.repository.facilities.RoomBookingRepository;
import org.springframework.web.bind.annotation.*;
import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/api/facilities/rooms")
public class RoomBookingController {
    private final RoomBookingRepository repo;
    public RoomBookingController(RoomBookingRepository repo) { this.repo = repo; }

    @GetMapping
    public List<RoomBooking> list() { return repo.findAll(); }

    @PostMapping
    public RoomBooking book(@RequestBody RoomBooking r) {
        r.setStatus("PENDING");
        r.setCreatedAt(Instant.now());
        return repo.save(r);
    }

    @PutMapping("/{id}/confirm")
    public RoomBooking confirm(@PathVariable Long id) {
        RoomBooking r = repo.findById(id).orElseThrow();
        r.setStatus("CONFIRMED");
        return repo.save(r);
    }

    @PutMapping("/{id}/cancel")
    public RoomBooking cancel(@PathVariable Long id) {
        RoomBooking r = repo.findById(id).orElseThrow();
        r.setStatus("CANCELLED");
        return repo.save(r);
    }
}
