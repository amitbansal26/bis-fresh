package in.gov.bis.platform.identity.web;

import in.gov.bis.platform.identity.domain.Role;
import in.gov.bis.platform.identity.domain.User;
import in.gov.bis.platform.identity.dto.UserDto;
import in.gov.bis.platform.identity.repository.RoleRepository;
import in.gov.bis.platform.identity.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class UserController {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @GetMapping
    public ResponseEntity<List<UserDto>> listAll() {
        List<UserDto> users = userRepository.findAll().stream()
                .map(this::toDto)
                .toList();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getById(@PathVariable UUID id) {
        return userRepository.findById(id)
                .map(this::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/roles")
    public ResponseEntity<UserDto> assignRoles(@PathVariable UUID id,
                                               @RequestBody @jakarta.validation.constraints.NotNull List<String> roleNames) {
        return userRepository.findById(id)
                .map(user -> {
                    Set<Role> roles = new HashSet<>();
                    for (String name : roleNames) {
                        roleRepository.findByName(name).ifPresent(roles::add);
                    }
                    user.setRoles(roles);
                    return ResponseEntity.ok(toDto(userRepository.save(user)));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    private UserDto toDto(User user) {
        List<String> roles = user.getRoles() == null ? List.of() :
                user.getRoles().stream().map(Role::getName).toList();
        return new UserDto(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getFullName(),
                user.getMobile(),
                user.isEnabled(),
                user.isEmailVerified(),
                user.getCreatedAt(),
                user.getUpdatedAt(),
                roles
        );
    }
}
