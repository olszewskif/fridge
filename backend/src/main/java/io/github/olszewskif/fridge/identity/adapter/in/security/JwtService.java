package io.github.olszewskif.fridge.identity.adapter.in.security;

import io.github.olszewskif.fridge.identity.application.port.outbound.TokenIssuer;
import io.github.olszewskif.fridge.identity.domain.Email;
import io.github.olszewskif.fridge.identity.domain.UserId;
import io.github.olszewskif.fridge.identity.domain.UserRole;
import io.github.olszewskif.fridge.shared.domain.DomainUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class JwtService implements TokenIssuer {

    @Value("${app.security.jwt.secret-key}")
    private String secretKey;

    @Value("${app.security.jwt.expiration-ms:86400000}")
    private long expirationMs;

    @Override
    public String generateToken(UserId userId, Email email, UserRole role) {
        Map<String, Object> claims = Map.of(
                "role", role.getName(),
                "id", userId.getValue()
        );
        return generateToken(claims, email);
    }

    private String generateToken(Map<String, Object> claims, Email email) {
        long currentMillis = System.currentTimeMillis();
        return Jwts.builder()
                .claims(claims)
                .subject(email.value())
                .issuedAt(new Date(currentMillis))
                .expiration(new Date(currentMillis + expirationMs))
                .signWith(getSignKey())
                .compact();
    }

    private SecretKey getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public boolean isExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }

    public <T> T extractClaim(String token, Function<Claims, T> resolver) {
        Claims claims = extractAllClaims(token);
        return resolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSignKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        String email = extractEmail(token);
        return (email.equals(userDetails.getUsername()) && !isExpired(token));
    }
}
