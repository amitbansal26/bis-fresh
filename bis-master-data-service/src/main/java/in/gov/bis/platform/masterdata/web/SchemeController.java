package in.gov.bis.platform.masterdata.web;

import in.gov.bis.platform.masterdata.dto.SchemeDto;
import in.gov.bis.platform.masterdata.service.SchemeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/master/schemes")
@RequiredArgsConstructor
public class SchemeController {

    private final SchemeService service;

    @GetMapping
    public List<SchemeDto> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<SchemeDto> findById(@PathVariable Long id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<SchemeDto> create(@RequestBody SchemeDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SchemeDto> update(@PathVariable Long id, @RequestBody SchemeDto dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
