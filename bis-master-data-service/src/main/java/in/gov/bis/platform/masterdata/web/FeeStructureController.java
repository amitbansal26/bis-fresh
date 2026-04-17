package in.gov.bis.platform.masterdata.web;

import in.gov.bis.platform.masterdata.dto.FeeStructureDto;
import in.gov.bis.platform.masterdata.service.FeeStructureService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/master/fee-structures")
@RequiredArgsConstructor
public class FeeStructureController {

    private final FeeStructureService service;

    @GetMapping
    public List<FeeStructureDto> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<FeeStructureDto> findById(@PathVariable Long id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<FeeStructureDto> create(@RequestBody FeeStructureDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<FeeStructureDto> update(@PathVariable Long id, @RequestBody FeeStructureDto dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
