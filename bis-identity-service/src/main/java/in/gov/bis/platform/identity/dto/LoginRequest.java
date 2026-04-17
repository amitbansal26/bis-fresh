package in.gov.bis.platform.identity.dto;

public record LoginRequest(
        String username,
        String password
) {}
