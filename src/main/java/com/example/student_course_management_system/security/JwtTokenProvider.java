package com.example.student_course_management_system.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProvider {
    private static final Logger LOGGER = LoggerFactory.getLogger(JwtTokenProvider.class);

    @Value(value = "${jwt.access-token.secret}")
    private String accessTokenSecret;

    @Value(value = "${jwt.access-token.expiration}")
    private int accessTokenExpiration;

    @Value(value = "${jwt.refresh-token.secret}")
    private String refreshTokenSecret;

    @Value(value = "${jwt.refresh-token.expiration}")
    private int refreshTokenExpiration;

    public String generateAccessToken(Authentication authentication) {
        return generateToken(authentication, accessTokenSecret, accessTokenExpiration);
    }

    public String generateRefreshToken(Authentication authentication) {
        return generateToken(authentication,refreshTokenSecret,refreshTokenExpiration);
    }

    public String generateToken(Authentication authentication, String secret, Integer expiration) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration);

        return Jwts.builder()
                .setSubject(Long.toString(userPrincipal.id()))
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(getSignKey(secret), SignatureAlgorithm.HS512)
                .compact();
    }

    private Key getSignKey(String secret) {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public Long getUserIdFromAccessToken(String token) {
        Claims claims = Jwts.parserBuilder().setSigningKey(getSignKey(accessTokenSecret)).build().parseClaimsJws(token).getBody();
        return Long.valueOf(claims.getSubject());
    }

    public Long getUserIdFromRefreshToken(String token) {
        Claims claims = Jwts.parserBuilder().setSigningKey(getSignKey(refreshTokenSecret)).build().parseClaimsJws(token).getBody();
        return Long.valueOf(claims.getSubject());
    }

    private Boolean isAccessTokenExpired(String token) {
        try {
            Claims claims = Jwts.parserBuilder().setSigningKey(getSignKey(accessTokenSecret)).build().parseClaimsJws(token).getBody();
            Date exp = claims.getExpiration();
            return exp.before(new Date());
        } catch (Exception e) {
            return true;
        }
    }

    private Boolean isRefreshTokenExpired(String token) {
        try {
            Claims claims = Jwts.parserBuilder().setSigningKey(getSignKey(refreshTokenSecret)).build().parseClaimsJws(token).getBody();
            Date exp = claims.getExpiration();
            return exp.before(new Date());
        } catch (Exception e) {
            return true;
        }
    }

    public boolean validateAccessToken(String authToken) {
        final Long userId = getUserIdFromAccessToken(authToken);
        return (userId > 0) && !isAccessTokenExpired(authToken);
    }

    public boolean validateRefreshToken(String authToken) {
        final Long userId = getUserIdFromRefreshToken(authToken);
        return (userId > 0) && !isRefreshTokenExpired(authToken);
    }

    public int getRefreshTokenExpiration() {
        return refreshTokenExpiration;
    }
}
