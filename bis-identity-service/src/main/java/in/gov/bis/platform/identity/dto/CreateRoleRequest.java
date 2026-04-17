package in.gov.bis.platform.identity.dto;

import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record CreateRoleRequest(
        @NotBlank String name,
        String description,
        List<Long> permissions
) {}
