package in.gov.bis.platform.masterdata.service;

import in.gov.bis.platform.masterdata.domain.Department;
import in.gov.bis.platform.masterdata.dto.DepartmentDto;
import in.gov.bis.platform.masterdata.repository.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DepartmentService {

    private final DepartmentRepository repository;

    public List<DepartmentDto> findAll() {
        return repository.findAll().stream().map(this::toDto).toList();
    }

    public Optional<DepartmentDto> findById(Long id) {
        return repository.findById(id).map(this::toDto);
    }

    public DepartmentDto create(DepartmentDto dto) {
        return toDto(repository.save(toEntity(dto)));
    }

    public DepartmentDto update(Long id, DepartmentDto dto) {
        Department entity = repository.findById(id)
                .orElseThrow(() -> new jakarta.persistence.EntityNotFoundException("Department not found: " + id));
        entity.setCode(dto.code());
        entity.setName(dto.name());
        entity.setOfficeCode(dto.officeCode());
        entity.setActive(dto.active());
        return toDto(repository.save(entity));
    }

    public void delete(Long id) {
        Department entity = repository.findById(id)
                .orElseThrow(() -> new jakarta.persistence.EntityNotFoundException("Department not found: " + id));
        entity.setActive(false);
        repository.save(entity);
    }

    private DepartmentDto toDto(Department e) {
        return new DepartmentDto(e.getId(), e.getCode(), e.getName(), e.getOfficeCode(), e.isActive());
    }

    private Department toEntity(DepartmentDto dto) {
        Department e = new Department();
        e.setCode(dto.code());
        e.setName(dto.name());
        e.setOfficeCode(dto.officeCode());
        e.setActive(dto.active());
        return e;
    }
}
