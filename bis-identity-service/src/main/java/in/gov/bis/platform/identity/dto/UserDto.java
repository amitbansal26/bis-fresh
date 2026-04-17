package in.gov.bis.platform.identity.dto;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record UserDto(
        UUID id,
        String username,
        String email,
        String fullName,
        String mobile,
        boolean enabled,
        boolean emailVerified,
        Instant createdAt,
        Instant updatedAt,
        List<String> roles
) {}
