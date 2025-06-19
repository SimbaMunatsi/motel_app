package com.rumbiemotel.motel.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys; // For secret key generation
import io.jsonwebtoken.security.SignatureException;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;

@Component
public class JwtUtil {
    // Ideally, load this from application.properties or a secure environment variable
    private final String SECRET = "yourVerySecretKeyThatIsAtLeast256BitsLongAndShouldBeSecurelyStored"; // IMPORTANT: Use a strong, random key in production

    // You should derive your key securely, e.g., using Keys.hmacShaKeyFor(secret.getBytes())
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET.getBytes());
    }

    // --- Methods for token parsing and validation ---

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        // Correct way for JJWT 0.12.x and newer
        return Jwts.parser()
                .verifyWith(getSigningKey()) // Replaces setSigningKey() for verification
                .build() // Don't forget to build the parser!
                .parseSignedClaims(token) // Replaces parseClaimsJws()
                .getPayload(); // Replaces getBody()
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public Boolean validateToken(String token, String username) {
        try {
            final String extractedUsername = extractUsername(token);
            return (extractedUsername.equals(username) && !isTokenExpired(token));
        } catch (SignatureException e) {
            // Log this: Invalid JWT signature
            System.err.println("Invalid JWT signature: " + e.getMessage());
            return false;
        } catch (ExpiredJwtException e) {
            // Log this: JWT token is expired
            System.err.println("JWT token is expired: " + e.getMessage());
            return false;
        } catch (IllegalArgumentException e) {
            // Log this: JWT token compact of handler are invalid
            System.err.println("JWT token compact of handler are invalid: " + e.getMessage());
            return false;
        }
    }

    // --- Example method for token generation (optional, but good to have) ---
    public String generateToken(String username) {
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10 hours expiration
                .signWith(getSigningKey())
                .compact();
    }
}
