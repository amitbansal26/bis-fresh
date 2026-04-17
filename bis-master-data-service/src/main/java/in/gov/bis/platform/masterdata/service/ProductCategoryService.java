package in.gov.bis.platform.masterdata.service;

import in.gov.bis.platform.masterdata.domain.ProductCategory;
import in.gov.bis.platform.masterdata.dto.ProductCategoryDto;
import in.gov.bis.platform.masterdata.repository.ProductCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductCategoryService {

    private final ProductCategoryRepository repository;

    public List<ProductCategoryDto> findAll() {
        return repository.findAll().stream().map(this::toDto).toList();
    }

    public Optional<ProductCategoryDto> findById(Long id) {
        return repository.findById(id).map(this::toDto);
    }

    public ProductCategoryDto create(ProductCategoryDto dto) {
        return toDto(repository.save(toEntity(dto)));
    }

    public ProductCategoryDto update(Long id, ProductCategoryDto dto) {
        ProductCategory entity = repository.findById(id)
                .orElseThrow(() -> new jakarta.persistence.EntityNotFoundException("ProductCategory not found: " + id));
        entity.setCode(dto.code());
        entity.setName(dto.name());
        entity.setSchemeCode(dto.schemeCode());
        entity.setParentCategoryId(dto.parentCategoryId());
        entity.setActive(dto.active());
        return toDto(repository.save(entity));
    }

    public void delete(Long id) {
        ProductCategory entity = repository.findById(id)
                .orElseThrow(() -> new jakarta.persistence.EntityNotFoundException("ProductCategory not found: " + id));
        entity.setActive(false);
        repository.save(entity);
    }

    private ProductCategoryDto toDto(ProductCategory e) {
        return new ProductCategoryDto(e.getId(), e.getCode(), e.getName(), e.getSchemeCode(), e.getParentCategoryId(), e.isActive());
    }

    private ProductCategory toEntity(ProductCategoryDto dto) {
        ProductCategory e = new ProductCategory();
        e.setCode(dto.code());
        e.setName(dto.name());
        e.setSchemeCode(dto.schemeCode());
        e.setParentCategoryId(dto.parentCategoryId());
        e.setActive(dto.active());
        return e;
    }
}
