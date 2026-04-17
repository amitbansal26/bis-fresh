package in.gov.bis.platform.integration.security;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.ECDSAVerifier;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jose.jwk.ECKey;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.time.Duration;
import java.time.Instant;
import java.util.Base64;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class DPoPProofValidationFilter extends OncePerRequestFilter {

    private static final String DPOP_PREFIX = "DPoP ";
    private static final Duration MAX_PROOF_AGE = Duration.ofMinutes(5);
    private static final Duration MAX_CLOCK_SKEW = Duration.ofSeconds(30);
    private final ConcurrentHashMap<String, Instant> seenJtis = new ConcurrentHashMap<>();

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof JwtAuthenticationToken jwtAuthenticationToken)) {
            filterChain.doFilter(request, response);
            return;
        }

        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        String dpopProof = request.getHeader("DPoP");
        if (!StringUtils.hasText(authorizationHeader) || !authorizationHeader.startsWith(DPOP_PREFIX)) {
            writeUnauthorized(response, "Missing DPoP authorization scheme");
            return;
        }
        if (!StringUtils.hasText(dpopProof)) {
            writeUnauthorized(response, "Missing DPoP proof header");
            return;
        }

        String accessToken = authorizationHeader.substring(DPOP_PREFIX.length()).trim();
        if (!StringUtils.hasText(accessToken)) {
            writeUnauthorized(response, "Missing access token");
            return;
        }

        try {
            validateProof(request, dpopProof, jwtAuthenticationToken.getToken(), accessToken);
        } catch (Exception ex) {
            writeUnauthorized(response, "Invalid DPoP proof");
            return;
        }

        filterChain.doFilter(request, response);
    }

    private void validateProof(HttpServletRequest request, String dpopProof, Jwt accessTokenJwt, String accessToken)
            throws ParseException, JOSEException {
        SignedJWT signedJWT = SignedJWT.parse(dpopProof);
        if (!"dpop+jwt".equalsIgnoreCase(signedJWT.getHeader().getType() == null
                ? null
                : signedJWT.getHeader().getType().toString())) {
            throw new JOSEException("Invalid typ");
        }

        JWK jwk = signedJWT.getHeader().getJWK();
        if (jwk == null) {
            throw new JOSEException("Missing JWK");
        }

        if (!signedJWT.verify(buildVerifier(jwk))) {
            throw new JOSEException("Signature verification failed");
        }

        JWTClaimsSet claims = signedJWT.getJWTClaimsSet();
        validateMethod(claims, request);
        validateUri(claims, request);
        validateIssuedAt(claims);
        validateJti(claims);
        validateAth(claims, accessToken);
        validateJkt(accessTokenJwt, jwk);
    }

    private void validateMethod(JWTClaimsSet claims, HttpServletRequest request) throws JOSEException, ParseException {
        String htm = claims.getStringClaim("htm");
        if (!request.getMethod().equalsIgnoreCase(htm)) {
            throw new JOSEException("Method mismatch");
        }
    }

    private void validateUri(JWTClaimsSet claims, HttpServletRequest request) throws JOSEException, ParseException {
        String htu = claims.getStringClaim("htu");
        Set<String> accepted = expectedUris(request);
        if (!accepted.contains(htu)) {
            throw new JOSEException("URI mismatch");
        }
    }

    private Set<String> expectedUris(HttpServletRequest request) {
        Set<String> uris = new LinkedHashSet<>();
        uris.add(request.getRequestURL().toString());

        String forwardedProto = request.getHeader("X-Forwarded-Proto");
        String forwardedHost = request.getHeader("X-Forwarded-Host");
        String forwardedUri = request.getHeader("X-Forwarded-Uri");
        if (StringUtils.hasText(forwardedProto) && StringUtils.hasText(forwardedHost) && StringUtils.hasText(forwardedUri)) {
            uris.add(forwardedProto + "://" + forwardedHost + forwardedUri);
        }

        String originalUri = request.getHeader("X-Original-Uri");
        if (StringUtils.hasText(forwardedProto) && StringUtils.hasText(forwardedHost) && StringUtils.hasText(originalUri)) {
            uris.add(forwardedProto + "://" + forwardedHost + originalUri);
        }

        return uris;
    }

    private void validateIssuedAt(JWTClaimsSet claims) throws JOSEException {
        Instant now = Instant.now();
        Instant issuedAt = claims.getIssueTime() == null ? null : claims.getIssueTime().toInstant();
        if (issuedAt == null || issuedAt.isBefore(now.minus(MAX_PROOF_AGE)) || issuedAt.isAfter(now.plus(MAX_CLOCK_SKEW))) {
            throw new JOSEException("Invalid iat");
        }
    }

    private void validateJti(JWTClaimsSet claims) throws JOSEException {
        String jti = claims.getJWTID();
        if (!StringUtils.hasText(jti)) {
            throw new JOSEException("Missing jti");
        }

        Instant now = Instant.now();
        Instant cutoff = now.minus(MAX_PROOF_AGE);
        seenJtis.entrySet().removeIf(entry -> entry.getValue().isBefore(cutoff));
        Instant previous = seenJtis.putIfAbsent(jti, now);
        if (previous != null && !previous.isBefore(cutoff)) {
            throw new JOSEException("Replay detected");
        }
    }

    private void validateAth(JWTClaimsSet claims, String accessToken) throws JOSEException, ParseException {
        String expectedAth = sha256(accessToken);
        String ath = claims.getStringClaim("ath");
        if (!expectedAth.equals(ath)) {
            throw new JOSEException("Invalid ath");
        }
    }

    @SuppressWarnings("unchecked")
    private void validateJkt(Jwt accessTokenJwt, JWK proofJwk) throws JOSEException {
        Object cnfClaim = accessTokenJwt.getClaims().get("cnf");
        if (!(cnfClaim instanceof Map<?, ?> cnf)) {
            throw new JOSEException("Missing cnf claim");
        }
        Object jkt = cnf.get("jkt");
        if (!(jkt instanceof String expectedJkt) || !StringUtils.hasText(expectedJkt)) {
            throw new JOSEException("Missing cnf.jkt");
        }

        String proofJkt = proofJwk.computeThumbprint().toString();
        if (!expectedJkt.equals(proofJkt)) {
            throw new JOSEException("cnf.jkt mismatch");
        }
    }

    private JWSVerifier buildVerifier(JWK jwk) throws JOSEException {
        if (jwk instanceof RSAKey rsaKey) {
            return new RSASSAVerifier(rsaKey.toRSAPublicKey());
        }
        if (jwk instanceof ECKey ecKey) {
            return new ECDSAVerifier(ecKey.toECPublicKey());
        }
        throw new JOSEException("Unsupported JWK type");
    }

    private String sha256(String value) throws JOSEException {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(value.getBytes(StandardCharsets.UTF_8));
            return Base64.getUrlEncoder().withoutPadding().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new JOSEException("SHA-256 unavailable", e);
        }
    }

    private void writeUnauthorized(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.getWriter().write("{\"error\":\"" + message + "\"}");
    }
}
