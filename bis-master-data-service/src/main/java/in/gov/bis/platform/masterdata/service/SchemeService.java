package in.gov.bis.platform.masterdata.service;

import in.gov.bis.platform.masterdata.domain.Scheme;
import in.gov.bis.platform.masterdata.dto.SchemeDto;
import in.gov.bis.platform.masterdata.repository.SchemeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SchemeService {

    private final SchemeRepository repository;

    public List<SchemeDto> findAll() {
        return repository.findAll().stream().map(this::toDto).toList();
    }

    public Optional<SchemeDto> findById(Long id) {
        return repository.findById(id).map(this::toDto);
    }

    public SchemeDto create(SchemeDto dto) {
        Scheme entity = toEntity(dto);
        return toDto(repository.save(entity));
    }

    public SchemeDto update(Long id, SchemeDto dto) {
        Scheme entity = repository.findById(id)
                .orElseThrow(() -> new jakarta.persistence.EntityNotFoundException("Scheme not found: " + id));
        entity.setCode(dto.code());
        entity.setName(dto.name());
        entity.setSchemeType(dto.schemeType());
        entity.setActive(dto.active());
        entity.setDescription(dto.description());
        return toDto(repository.save(entity));
    }

    public void delete(Long id) {
        Scheme entity = repository.findById(id)
                .orElseThrow(() -> new jakarta.persistence.EntityNotFoundException("Scheme not found: " + id));
        entity.setActive(false);
        repository.save(entity);
    }

    private SchemeDto toDto(Scheme e) {
        return new SchemeDto(e.getId(), e.getCode(), e.getName(), e.getSchemeType(), e.isActive(), e.getDescription());
    }

    private Scheme toEntity(SchemeDto dto) {
        Scheme e = new Scheme();
        e.setCode(dto.code());
        e.setName(dto.name());
        e.setSchemeType(dto.schemeType());
        e.setActive(dto.active());
        e.setDescription(dto.description());
        return e;
    }
}
