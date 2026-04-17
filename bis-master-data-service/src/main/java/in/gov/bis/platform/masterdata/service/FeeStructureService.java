package in.gov.bis.platform.masterdata.service;

import in.gov.bis.platform.masterdata.domain.FeeStructure;
import in.gov.bis.platform.masterdata.dto.FeeStructureDto;
import in.gov.bis.platform.masterdata.repository.FeeStructureRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FeeStructureService {

    private final FeeStructureRepository repository;

    public List<FeeStructureDto> findAll() {
        return repository.findAll().stream().map(this::toDto).toList();
    }

    public Optional<FeeStructureDto> findById(Long id) {
        return repository.findById(id).map(this::toDto);
    }

    public FeeStructureDto create(FeeStructureDto dto) {
        return toDto(repository.save(toEntity(dto)));
    }

    public FeeStructureDto update(Long id, FeeStructureDto dto) {
        FeeStructure entity = repository.findById(id)
                .orElseThrow(() -> new jakarta.persistence.EntityNotFoundException("FeeStructure not found: " + id));
        entity.setSchemeCode(dto.schemeCode());
        entity.setFeeType(dto.feeType());
        entity.setAmount(dto.amount());
        entity.setCurrency(dto.currency());
        entity.setEffectiveDate(dto.effectiveDate());
        entity.setActive(dto.active());
        return toDto(repository.save(entity));
    }

    public void delete(Long id) {
        FeeStructure entity = repository.findById(id)
                .orElseThrow(() -> new jakarta.persistence.EntityNotFoundException("FeeStructure not found: " + id));
        entity.setActive(false);
        repository.save(entity);
    }

    private FeeStructureDto toDto(FeeStructure e) {
        return new FeeStructureDto(e.getId(), e.getSchemeCode(), e.getFeeType(), e.getAmount(),
                e.getCurrency(), e.getEffectiveDate(), e.isActive());
    }

    private FeeStructure toEntity(FeeStructureDto dto) {
        FeeStructure e = new FeeStructure();
        e.setSchemeCode(dto.schemeCode());
        e.setFeeType(dto.feeType());
        e.setAmount(dto.amount());
        e.setCurrency(dto.currency() != null ? dto.currency() : "INR");
        e.setEffectiveDate(dto.effectiveDate());
        e.setActive(dto.active());
        return e;
    }
}
