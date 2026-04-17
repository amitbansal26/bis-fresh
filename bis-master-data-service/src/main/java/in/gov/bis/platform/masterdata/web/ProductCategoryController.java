package in.gov.bis.platform.masterdata.web;

import in.gov.bis.platform.masterdata.dto.ProductCategoryDto;
import in.gov.bis.platform.masterdata.service.ProductCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/master/product-categories")
@RequiredArgsConstructor
public class ProductCategoryController {

    private final ProductCategoryService service;

    @GetMapping
    public List<ProductCategoryDto> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductCategoryDto> findById(@PathVariable Long id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ProductCategoryDto> create(@RequestBody ProductCategoryDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductCategoryDto> update(@PathVariable Long id, @RequestBody ProductCategoryDto dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
