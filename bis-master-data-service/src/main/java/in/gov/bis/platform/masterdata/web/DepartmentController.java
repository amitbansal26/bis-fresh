package in.gov.bis.platform.masterdata.web;

import in.gov.bis.platform.masterdata.dto.DepartmentDto;
import in.gov.bis.platform.masterdata.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/master/departments")
@RequiredArgsConstructor
public class DepartmentController {

    private final DepartmentService service;

    @GetMapping
    public List<DepartmentDto> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<DepartmentDto> findById(@PathVariable Long id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<DepartmentDto> create(@RequestBody DepartmentDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DepartmentDto> update(@PathVariable Long id, @RequestBody DepartmentDto dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
