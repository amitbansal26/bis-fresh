package in.gov.bis.platform.masterdata.dto;

public record OfficeDto(
        Long id,
        String officeCode,
        String officeName,
        String officeType,
        String state,
        String city,
        String address,
        String pinCode,
        boolean active
) {}
