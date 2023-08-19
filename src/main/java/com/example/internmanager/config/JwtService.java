package com.example.internmanager.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
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

    public TokenType extractTokenType(String token) {
        String tokenTypeString = extractClaimValue(token, claims -> claims.get("token_type", String.class));
        if(tokenTypeString.equals(TokenType.ACCESS.name())) {
            return TokenType.ACCESS;
        } else if (tokenTypeString.equals(TokenType.REFRESH.name())) {
            return TokenType.REFRESH;
        } else throw new MalformedJwtException("Invalid token type");
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

    private String generateTokens(TokenType tokenType, HashMap<String, Object> additionalClaims, UserDetails userDetails, Long tokenExpirationMillis) {
        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + tokenExpirationMillis);

        additionalClaims.put("token_type", tokenType.name());

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

    public String generateAccessToken(HashMap<String, Object> additionalClaims, UserDetails userDetails) {
        return generateTokens(TokenType.ACCESS, additionalClaims, userDetails, accessTokenExpirationMillis);
    }

    public String generateRefreshToken(UserDetails userDetails) {
        return generateTokens(TokenType.REFRESH, new HashMap<>(), userDetails, refreshTokenExpirationMillis);
    }

    public boolean isAccessTokenValid(String token, UserDetails userDetails) {
        if(extractTokenType(token) != TokenType.ACCESS) return false;
        return (extractUsername(token).equals(userDetails.getUsername())) && isTokenNotExpired(token);
    }

    public boolean isRefreshTokenValid(String token, UserDetails userDetails) {
        if(extractTokenType(token) != TokenType.REFRESH) return false;
        return (extractUsername(token).equals(userDetails.getUsername())) && isTokenNotExpired(token);
    }

    public boolean isTokenNotExpired(String token) {
        return !extractExpiration(token).before(new Date());
    }
}
