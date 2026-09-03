package dev.team08.backend.utility;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtility {
    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;

    // Refresh window time (e.g., 15 minutes before expiration)
    private static final long REFRESH_WINDOW = 900000; // 15 minutes in milliseconds

    private SecretKey signingKey() {
        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // Generate JWT token
    public String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        return doGenerateToken(claims, username);
    }

    // Generate token based on claims
    private String doGenerateToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(signingKey())
                .compact();
    }

    // Extract username from token
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Extract expiration date from token
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // Extract claims
    public <T> T extractClaim(String token, java.util.function.Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // Extract all claims
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(signingKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // Check if token has expired
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // Check if token needs refreshing
    public boolean shouldRefreshToken(String token) {
        try {
            Date expiration = extractExpiration(token);
            long timeToExpiration = expiration.getTime() - System.currentTimeMillis();
            return timeToExpiration <= REFRESH_WINDOW;  // Token is within the refresh window
        } catch (JwtException | IllegalArgumentException e) {
            // Malformed / expired / wrong-signature tokens cannot be refreshed.
            return false;
        }
    }

    // Validate token. Expired, malformed, or wrong-signature tokens are simply
    // "not valid" — they should never bubble an exception up to controllers,
    // because that turns into a 500 Internal Server Error for the client when
    // the correct response is a clean 401 Unauthorized.
    public boolean isTokenValid(String token, String username) {
        try {
            final String extractedUsername = extractUsername(token);
            return extractedUsername.equals(username) && !isTokenExpired(token);
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    // Null-safe accessor for callers (filters, services) that only need a
    // username and don't want to wrap every call in their own try/catch.
    public String extractUsernameSafe(String token) {
        try {
            return extractUsername(token);
        } catch (JwtException | IllegalArgumentException e) {
            return null;
        }
    }
}
