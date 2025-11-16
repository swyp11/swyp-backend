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
    private final long expirationMs;

    public JwtUtil(
            @Value("${spring.jwt.secret}") String secret,
            @Value("${spring.jwt.expiration}") long expirationMs
    ) {
        // SecretKeySpec 대신 Keys.hmacShaKeyFor 사용 (JJWT 권장 방식)
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.expirationMs = expirationMs;
    }

    public String createToken(String username, String role) {

        return Jwts.builder()
                .claim("username", username)
                .claim("role", role)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expirationMs))
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

    // Expiration 시간 getter (초 단위)
    public long getExpirationInSeconds() {
        return expirationMs / 1000;
    }

    /**
     * 이메일 인증용 임시 토큰 생성 (5분 유효)
     * @param email 인증된 이메일
     * @param purpose 용도 (SIGNUP, PASSWORD_RESET)
     * @return 임시 JWT 토큰
     */
    public String createEmailVerificationToken(String email, String purpose) {
        return Jwts.builder()
                .claim("email", email)
                .claim("purpose", purpose)
                .claim("type", "EMAIL_VERIFICATION")
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 5 * 60 * 1000)) // 5분
                .signWith(secretKey)
                .compact();
    }

    /**
     * 이메일 인증 토큰 검증
     * @param token 임시 토큰
     * @param expectedEmail 예상 이메일
     * @param expectedPurpose 예상 용도
     * @return 검증 성공 여부
     */
    public boolean verifyEmailVerificationToken(String token, String expectedEmail, String expectedPurpose) {
        try {
            Claims claims = verify(token).getPayload();
            String type = claims.get("type", String.class);
            String email = claims.get("email", String.class);
            String purpose = claims.get("purpose", String.class);

            return "EMAIL_VERIFICATION".equals(type)
                    && expectedEmail.equals(email)
                    && expectedPurpose.equals(purpose);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 토큰에서 이메일 추출
     */
    public String getEmailFromToken(String token) {
        return verify(token).getPayload().get("email", String.class);
    }

    /**
     * 토큰에서 용도 추출
     */
    public String getPurposeFromToken(String token) {
        return verify(token).getPayload().get("purpose", String.class);
    }

}

