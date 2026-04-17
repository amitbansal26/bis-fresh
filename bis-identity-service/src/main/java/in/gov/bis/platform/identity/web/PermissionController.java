package in.gov.bis.platform.identity.web;

import in.gov.bis.platform.identity.domain.Permission;
import in.gov.bis.platform.identity.dto.CreatePermissionRequest;
import in.gov.bis.platform.identity.dto.PermissionDto;
import in.gov.bis.platform.identity.repository.PermissionRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/permissions")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class PermissionController {

    private final PermissionRepository permissionRepository;

    /** Returns all permissions grouped by module. */
    @GetMapping
    public ResponseEntity<Map<String, List<PermissionDto>>> listGroupedByModule() {
        Map<String, List<PermissionDto>> grouped = permissionRepository.findAll().stream()
                .map(p -> new PermissionDto(p.getId(), p.getName(), p.getModule(), p.getAction()))
                .collect(Collectors.groupingBy(PermissionDto::module));
        return ResponseEntity.ok(grouped);
    }

    @PostMapping
    public ResponseEntity<PermissionDto> create(@Valid @RequestBody CreatePermissionRequest request) {
        Permission permission = Permission.builder()
                .name(request.name())
                .module(request.module())
                .action(request.action())
                .build();
        Permission saved = permissionRepository.save(permission);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new PermissionDto(saved.getId(), saved.getName(), saved.getModule(), saved.getAction()));
    }
}
