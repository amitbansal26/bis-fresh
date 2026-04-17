package in.gov.bis.platform.masterdata.dto;

public record ProductCategoryDto(
        Long id,
        String code,
        String name,
        String schemeCode,
        Long parentCategoryId,
        boolean active
) {}
