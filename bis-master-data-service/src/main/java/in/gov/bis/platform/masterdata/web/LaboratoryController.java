package in.gov.bis.platform.masterdata.web;

import in.gov.bis.platform.masterdata.dto.LaboratoryDto;
import in.gov.bis.platform.masterdata.service.LaboratoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/master/laboratories")
@RequiredArgsConstructor
public class LaboratoryController {

    private final LaboratoryService service;

    @GetMapping
    public List<LaboratoryDto> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<LaboratoryDto> findById(@PathVariable Long id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<LaboratoryDto> create(@RequestBody LaboratoryDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<LaboratoryDto> update(@PathVariable Long id, @RequestBody LaboratoryDto dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
