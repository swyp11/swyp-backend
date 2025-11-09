package com.swyp.wedding.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {

    private final SecretKey secretKey;
    private static final long EXPIRATION_MS = 1000L * 60 * 60;

    public JwtUtil(@Value("${spring.jwt.secret}") String secret) {
        // SecretKeySpec 대신 Keys.hmacShaKeyFor 사용 (JJWT 권장 방식)
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String createToken(String username, String role) {

        return Jwts.builder()
                .claim("username", username)
                .claim("role", role)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_MS))
                .signWith(secretKey)
                .compact();
    }

    /**
     * JWT 검증 (verify 역할)
     * 토큰의 유효성/서명/만료를 모두 검사함
     */
    public Jws<Claims> verify(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token);
    }

    // 사용자 이름 추출
    public String getUsername(String token) {
        return verify(token).getPayload().get("username", String.class);
    }

    // 사용자 권한 추출
    public String getRole(String token) {
        return verify(token).getPayload().get("role", String.class);
    }

    // 만료 여부 확인
    public boolean isExpired(String token) {
        Date expiration = verify(token).getPayload().getExpiration();
        return expiration.before(new Date());
    }

}
