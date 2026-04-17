package in.gov.bis.platform.identity.dto;

import jakarta.validation.constraints.NotBlank;

public record CreatePermissionRequest(
        @NotBlank String name,
        @NotBlank String module,
        @NotBlank String action
) {}
