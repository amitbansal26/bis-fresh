package in.gov.bis.platform.masterdata.service;

import in.gov.bis.platform.masterdata.domain.Office;
import in.gov.bis.platform.masterdata.dto.OfficeDto;
import in.gov.bis.platform.masterdata.repository.OfficeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OfficeService {

    private final OfficeRepository repository;

    public List<OfficeDto> findAll() {
        return repository.findAll().stream().map(this::toDto).toList();
    }

    public Optional<OfficeDto> findById(Long id) {
        return repository.findById(id).map(this::toDto);
    }

    public OfficeDto create(OfficeDto dto) {
        return toDto(repository.save(toEntity(dto)));
    }

    public OfficeDto update(Long id, OfficeDto dto) {
        Office entity = repository.findById(id)
                .orElseThrow(() -> new jakarta.persistence.EntityNotFoundException("Office not found: " + id));
        entity.setOfficeCode(dto.officeCode());
        entity.setOfficeName(dto.officeName());
        entity.setOfficeType(dto.officeType());
        entity.setState(dto.state());
        entity.setCity(dto.city());
        entity.setAddress(dto.address());
        entity.setPinCode(dto.pinCode());
        entity.setActive(dto.active());
        return toDto(repository.save(entity));
    }

    public void delete(Long id) {
        Office entity = repository.findById(id)
                .orElseThrow(() -> new jakarta.persistence.EntityNotFoundException("Office not found: " + id));
        entity.setActive(false);
        repository.save(entity);
    }

    private OfficeDto toDto(Office e) {
        return new OfficeDto(e.getId(), e.getOfficeCode(), e.getOfficeName(), e.getOfficeType(),
                e.getState(), e.getCity(), e.getAddress(), e.getPinCode(), e.isActive());
    }

    private Office toEntity(OfficeDto dto) {
        Office e = new Office();
        e.setOfficeCode(dto.officeCode());
        e.setOfficeName(dto.officeName());
        e.setOfficeType(dto.officeType());
        e.setState(dto.state());
        e.setCity(dto.city());
        e.setAddress(dto.address());
        e.setPinCode(dto.pinCode());
        e.setActive(dto.active());
        return e;
    }
}
