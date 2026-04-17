package in.gov.bis.platform.masterdata.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record FeeStructureDto(
        Long id,
        String schemeCode,
        String feeType,
        BigDecimal amount,
        String currency,
        LocalDate effectiveDate,
        boolean active
) {}
