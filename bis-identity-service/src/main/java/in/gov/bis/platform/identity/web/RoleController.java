package in.gov.bis.platform.identity.web;

import in.gov.bis.platform.identity.domain.Permission;
import in.gov.bis.platform.identity.domain.Role;
import in.gov.bis.platform.identity.dto.CreateRoleRequest;
import in.gov.bis.platform.identity.dto.PermissionDto;
import in.gov.bis.platform.identity.dto.RoleDto;
import in.gov.bis.platform.identity.repository.PermissionRepository;
import in.gov.bis.platform.identity.repository.RoleRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class RoleController {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    @GetMapping
    public ResponseEntity<List<RoleDto>> listAll() {
        return ResponseEntity.ok(roleRepository.findAll().stream().map(this::toDto).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoleDto> getById(@PathVariable Long id) {
        return roleRepository.findById(id)
                .map(this::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<RoleDto> create(@Valid @RequestBody CreateRoleRequest request) {
        Role role = Role.builder()
                .name(request.name())
                .description(request.description())
                .permissions(resolvePermissions(request.permissions()))
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(toDto(roleRepository.save(role)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RoleDto> update(@PathVariable Long id,
                                          @Valid @RequestBody CreateRoleRequest request) {
        return roleRepository.findById(id)
                .map(role -> {
                    role.setName(request.name());
                    role.setDescription(request.description());
                    role.setPermissions(resolvePermissions(request.permissions()));
                    return ResponseEntity.ok(toDto(roleRepository.save(role)));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!roleRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        roleRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{roleId}/permissions/{permissionId}")
    public ResponseEntity<RoleDto> addPermission(@PathVariable Long roleId,
                                                  @PathVariable Long permissionId) {
        Role role = roleRepository.findById(roleId).orElse(null);
        Permission permission = permissionRepository.findById(permissionId).orElse(null);
        if (role == null || permission == null) {
            return ResponseEntity.notFound().build();
        }
        role.getPermissions().add(permission);
        return ResponseEntity.ok(toDto(roleRepository.save(role)));
    }

    @DeleteMapping("/{roleId}/permissions/{permissionId}")
    public ResponseEntity<RoleDto> removePermission(@PathVariable Long roleId,
                                                     @PathVariable Long permissionId) {
        Role role = roleRepository.findById(roleId).orElse(null);
        if (role == null) {
            return ResponseEntity.notFound().build();
        }
        role.getPermissions().removeIf(p -> p.getId().equals(permissionId));
        return ResponseEntity.ok(toDto(roleRepository.save(role)));
    }

    private Set<Permission> resolvePermissions(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return new HashSet<>();
        }
        return new HashSet<>(permissionRepository.findAllById(ids));
    }

    private RoleDto toDto(Role role) {
        List<PermissionDto> permissions = role.getPermissions() == null ? List.of() :
                role.getPermissions().stream()
                        .map(p -> new PermissionDto(p.getId(), p.getName(), p.getModule(), p.getAction()))
                        .toList();
        return new RoleDto(role.getId(), role.getName(), role.getDescription(), permissions);
    }
}
