package in.gov.bis.platform.identity.dto;

import java.util.List;

public record AuthResponse(
        String token,
        String username,
        List<String> roles
) {}
