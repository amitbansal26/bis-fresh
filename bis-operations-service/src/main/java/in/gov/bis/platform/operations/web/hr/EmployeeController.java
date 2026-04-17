package in.gov.bis.platform.operations.web.hr;

import in.gov.bis.platform.operations.domain.hr.Employee;
import in.gov.bis.platform.operations.repository.hr.EmployeeRepository;
import org.springframework.web.bind.annotation.*;
import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/api/hr/employees")
public class EmployeeController {
    private final EmployeeRepository repo;
    public EmployeeController(EmployeeRepository repo) { this.repo = repo; }

    @GetMapping
    public List<Employee> list(@RequestParam(required = false) String officeCode) {
        return officeCode != null ? repo.findByOfficeCode(officeCode) : repo.findAll();
    }

    @PostMapping
    public Employee create(@RequestBody Employee e) {
        e.setCreatedAt(Instant.now());
        return repo.save(e);
    }

    @GetMapping("/{id}")
    public Employee get(@PathVariable Long id) {
        return repo.findById(id).orElseThrow();
    }

    @PutMapping("/{id}")
    public Employee update(@PathVariable Long id, @RequestBody Employee e) {
        e.setId(id);
        return repo.save(e);
    }
}
