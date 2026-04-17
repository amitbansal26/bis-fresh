package in.gov.bis.platform.masterdata.dto;

public record LaboratoryDto(
        Long id,
        String labCode,
        String labName,
        String labType,
        String officeCode,
        String state,
        String city,
        boolean nabl,
        String disciplines,
        boolean active
) {}
