package in.gov.bis.platform.masterdata.service;

import in.gov.bis.platform.masterdata.domain.Standard;
import in.gov.bis.platform.masterdata.dto.StandardDto;
import in.gov.bis.platform.masterdata.repository.StandardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StandardService {

    private final StandardRepository repository;

    public List<StandardDto> findAll() {
        return repository.findAll().stream().map(this::toDto).toList();
    }

    public Optional<StandardDto> findById(Long id) {
        return repository.findById(id).map(this::toDto);
    }

    public StandardDto create(StandardDto dto) {
        return toDto(repository.save(toEntity(dto)));
    }

    public StandardDto update(Long id, StandardDto dto) {
        Standard entity = repository.findById(id)
                .orElseThrow(() -> new jakarta.persistence.EntityNotFoundException("Standard not found: " + id));
        entity.setStandardNo(dto.standardNo());
        entity.setTitle(dto.title());
        entity.setVersion(dto.version());
        entity.setStatus(dto.status());
        entity.setPublishedDate(dto.publishedDate());
        entity.setApplicableScheme(dto.applicableScheme());
        return toDto(repository.save(entity));
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    private StandardDto toDto(Standard e) {
        return new StandardDto(e.getId(), e.getStandardNo(), e.getTitle(), e.getVersion(),
                e.getStatus(), e.getPublishedDate(), e.getApplicableScheme());
    }

    private Standard toEntity(StandardDto dto) {
        Standard e = new Standard();
        e.setStandardNo(dto.standardNo());
        e.setTitle(dto.title());
        e.setVersion(dto.version());
        e.setStatus(dto.status());
        e.setPublishedDate(dto.publishedDate());
        e.setApplicableScheme(dto.applicableScheme());
        return e;
    }
}
