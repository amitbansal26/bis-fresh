package in.gov.bis.platform.masterdata.service;

import in.gov.bis.platform.masterdata.domain.Laboratory;
import in.gov.bis.platform.masterdata.dto.LaboratoryDto;
import in.gov.bis.platform.masterdata.repository.LaboratoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LaboratoryService {

    private final LaboratoryRepository repository;

    public List<LaboratoryDto> findAll() {
        return repository.findAll().stream().map(this::toDto).toList();
    }

    public Optional<LaboratoryDto> findById(Long id) {
        return repository.findById(id).map(this::toDto);
    }

    public LaboratoryDto create(LaboratoryDto dto) {
        return toDto(repository.save(toEntity(dto)));
    }

    public LaboratoryDto update(Long id, LaboratoryDto dto) {
        Laboratory entity = repository.findById(id)
                .orElseThrow(() -> new jakarta.persistence.EntityNotFoundException("Laboratory not found: " + id));
        entity.setLabCode(dto.labCode());
        entity.setLabName(dto.labName());
        entity.setLabType(dto.labType());
        entity.setOfficeCode(dto.officeCode());
        entity.setState(dto.state());
        entity.setCity(dto.city());
        entity.setNabl(dto.nabl());
        entity.setDisciplines(dto.disciplines());
        entity.setActive(dto.active());
        return toDto(repository.save(entity));
    }

    public void delete(Long id) {
        Laboratory entity = repository.findById(id)
                .orElseThrow(() -> new jakarta.persistence.EntityNotFoundException("Laboratory not found: " + id));
        entity.setActive(false);
        repository.save(entity);
    }

    private LaboratoryDto toDto(Laboratory e) {
        return new LaboratoryDto(e.getId(), e.getLabCode(), e.getLabName(), e.getLabType(),
                e.getOfficeCode(), e.getState(), e.getCity(), e.isNabl(), e.getDisciplines(), e.isActive());
    }

    private Laboratory toEntity(LaboratoryDto dto) {
        Laboratory e = new Laboratory();
        e.setLabCode(dto.labCode());
        e.setLabName(dto.labName());
        e.setLabType(dto.labType());
        e.setOfficeCode(dto.officeCode());
        e.setState(dto.state());
        e.setCity(dto.city());
        e.setNabl(dto.nabl());
        e.setDisciplines(dto.disciplines());
        e.setActive(dto.active());
        return e;
    }
}
