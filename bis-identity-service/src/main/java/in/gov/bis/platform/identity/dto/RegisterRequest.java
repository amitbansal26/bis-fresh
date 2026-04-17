package in.gov.bis.platform.identity.dto;

public record RegisterRequest(
        String username,
        String email,
        String password,
        String fullName,
        String mobile
) {}
