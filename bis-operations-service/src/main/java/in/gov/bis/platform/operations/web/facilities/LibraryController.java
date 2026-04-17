package in.gov.bis.platform.operations.web.facilities;

import in.gov.bis.platform.operations.domain.facilities.LibraryBook;
import in.gov.bis.platform.operations.repository.facilities.LibraryBookRepository;
import org.springframework.web.bind.annotation.*;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/facilities/library")
public class LibraryController {
    private final LibraryBookRepository repo;
    public LibraryController(LibraryBookRepository repo) { this.repo = repo; }

    @GetMapping
    public List<LibraryBook> list(@RequestParam(required = false) Boolean available) {
        return available != null ? repo.findByAvailable(available) : repo.findAll();
    }

    @PostMapping
    public LibraryBook addBook(@RequestBody LibraryBook b) {
        b.setAvailable(true);
        b.setCreatedAt(Instant.now());
        return repo.save(b);
    }

    @PostMapping("/{id}/issue")
    public LibraryBook issue(@PathVariable Long id, @RequestParam String issuedTo) {
        LibraryBook b = repo.findById(id).orElseThrow();
        b.setAvailable(false);
        b.setIssuedTo(issuedTo);
        b.setIssueDate(LocalDate.now());
        b.setReturnDate(LocalDate.now().plusDays(14));
        return repo.save(b);
    }

    @PostMapping("/{id}/return")
    public LibraryBook returnBook(@PathVariable Long id) {
        LibraryBook b = repo.findById(id).orElseThrow();
        b.setAvailable(true);
        b.setIssuedTo(null);
        b.setIssueDate(null);
        b.setReturnDate(null);
        return repo.save(b);
    }
}
