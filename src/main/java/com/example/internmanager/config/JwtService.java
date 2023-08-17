package com.example.internmanager.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;
import java.util.function.Function;

@Service
public class JwtService {
    @Value("${application.security.jwt.base64SecretKey}")
    private String base64SecretKey;

    @Value("${application.security.jwt.accessTokenExpirationMillis}")
    private long accessTokenExpirationMillis;

    @Value("${application.security.jwt.refreshTokenExpirationMillis}")
    private long refreshTokenExpirationMillis;

    public String extractUsername(String token) {
        return extractClaimValue(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaimValue(token, Claims::getExpiration);
    }

    private Claims extractClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getHmacShaSecretKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private <T> T extractClaimValue(String token, Function<Claims, T> claimsResolver) {
        Claims claims = extractClaims(token);
        return claimsResolver.apply(claims);
    }

    private SecretKey getHmacShaSecretKey() {
        byte[] keyBytes = Decoders.BASE64.decode(base64SecretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private String generateTokens(HashMap<String, Objects> additionalClaims, UserDetails userDetails, Long tokenExpirationMillis) {
        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + tokenExpirationMillis);
        return Jwts.builder()
                .setClaims(additionalClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(expirationDate)
                .signWith(getHmacShaSecretKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateAccessToken(UserDetails userDetails) {
        return generateAccessToken(new HashMap<>(), userDetails);
    }

    public String generateAccessToken(HashMap<String, Objects> additionalClaims, UserDetails userDetails) {
        return generateTokens(additionalClaims, userDetails, accessTokenExpirationMillis);
    }

    public String generateRefreshToken(UserDetails userDetails) {
        return generateTokens(new HashMap<>(), userDetails, refreshTokenExpirationMillis);
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        return (extractUsername(token).equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }
}
