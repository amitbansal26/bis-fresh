package in.gov.bis.platform.masterdata.dto;

public record SchemeDto(
        Long id,
        String code,
        String name,
        String schemeType,
        boolean active,
        String description
) {}
