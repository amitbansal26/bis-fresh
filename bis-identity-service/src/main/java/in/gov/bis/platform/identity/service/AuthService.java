package in.gov.bis.platform.identity.service;

import in.gov.bis.platform.identity.domain.User;
import in.gov.bis.platform.identity.dto.AuthResponse;
import in.gov.bis.platform.identity.dto.RegisterRequest;
import in.gov.bis.platform.identity.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.username())) {
            throw new IllegalArgumentException("Registration failed");
        }
        if (userRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("Registration failed");
        }

        User user = User.builder()
                .username(request.username())
                .email(request.email())
                .passwordHash(passwordEncoder.encode(request.password()))
                .fullName(request.fullName())
                .mobile(request.mobile())
                .enabled(true)
                .emailVerified(false)
                .roles(new HashSet<>())
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

        userRepository.save(user);

        return new AuthResponse(null, user.getUsername(), List.of());
    }
}
