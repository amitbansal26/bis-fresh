package in.gov.bis.platform.masterdata.dto;

public record DepartmentDto(
        Long id,
        String code,
        String name,
        String officeCode,
        boolean active
) {}
