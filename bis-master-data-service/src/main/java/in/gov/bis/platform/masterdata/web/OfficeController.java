package in.gov.bis.platform.masterdata.web;

import in.gov.bis.platform.masterdata.dto.OfficeDto;
import in.gov.bis.platform.masterdata.service.OfficeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/master/offices")
@RequiredArgsConstructor
public class OfficeController {

    private final OfficeService service;

    @GetMapping
    public List<OfficeDto> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<OfficeDto> findById(@PathVariable Long id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<OfficeDto> create(@RequestBody OfficeDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<OfficeDto> update(@PathVariable Long id, @RequestBody OfficeDto dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
