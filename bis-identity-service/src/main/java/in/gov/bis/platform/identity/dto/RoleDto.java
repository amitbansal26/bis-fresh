package in.gov.bis.platform.identity.dto;

import java.util.List;

public record RoleDto(Long id, String name, String description, List<PermissionDto> permissions) {}
