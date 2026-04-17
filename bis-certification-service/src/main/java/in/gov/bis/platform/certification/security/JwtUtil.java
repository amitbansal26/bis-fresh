package in.gov.bis.platform.certification.security;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
@Component
public class JwtUtil {
    @Value("${app.jwt.secret}")
    private String secret;
    private SecretKey key() { return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret)); }
    public String extractUsername(String token) {
        return Jwts.parser().verifyWith(key()).build().parseSignedClaims(token).getPayload().getSubject();
    }
    public boolean validateToken(String token, UserDetails userDetails) {
        try { return extractUsername(token).equals(userDetails.getUsername()); } catch (JwtException e) { return false; }
    }
}
