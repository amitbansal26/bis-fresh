package in.gov.bis.platform.masterdata.dto;

import java.time.LocalDate;

public record StandardDto(
        Long id,
        String standardNo,
        String title,
        String version,
        String status,
        LocalDate publishedDate,
        String applicableScheme
) {}
