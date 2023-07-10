package com.laan.sportsda.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtUtil {

    private static final String TOKEN_HEADER = "Authorization";
    private static final String TOKEN_PREFIX = "Bearer ";

    private final PropertyUtil propertyUtil;

    public String generateToken(final String subject, final String id, final Date issuedAt, final Date expiration, final Map<String, Object> claims) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setId(id)
                .setIssuedAt(issuedAt)
                .setExpiration(expiration)
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public String extractId(String token) {
        return extractClaim(token, Claims::getId);
    }

    public List<String> extractPermissions(String token) {
        return extractClaim(token, claims -> claims.get("permissions", List.class));
    }

    public boolean validateToken(String token) throws AuthenticationException {
        boolean validated = false;
        try {
            Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(token);
            validated = true;
        } catch (UnsupportedJwtException e) {
            log.error("UnsupportedJwtException occurred due to the claimsJws argument does not represent a Claims JWS");
        } catch (MalformedJwtException e) {
            log.error("MalformedJwtException occurred due to the claimsJws string is not a valid JWS");
        } catch (SecurityException e) {
            log.error("SecurityException occurred due to the claimsJws JWS signature validation fails");
        } catch (ExpiredJwtException e) {
            log.error("ExpiredJwtException occurred due to the specified JWT is a Claims JWT and the Claims has an expiration time before the time this method is invoked");
        } catch (IllegalArgumentException e) {
            log.error("IllegalArgumentException occurred due to the claimsJws string is null or empty or only whitespace");
        } catch (Exception e) {
            log.error("Exception occurred when validating the jwt token", e);
        }
        return validated;
    }

    public String getTokenFromRequest(HttpServletRequest request) {
        String authHeader = request.getHeader(TOKEN_HEADER);

        if (authHeader != null && authHeader.startsWith(TOKEN_PREFIX)) {
            return authHeader.substring(TOKEN_PREFIX.length());
        }

        return null;
    }

    private Key getSignKey() {
        byte[] keyArray = Decoders.BASE64.decode(propertyUtil.getJwtSecretKey());
        return Keys.hmacShaKeyFor(keyArray);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
