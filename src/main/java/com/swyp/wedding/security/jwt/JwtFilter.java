package com.swyp.wedding.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;

@RequiredArgsConstructor
@Schema(description = "JWT AuthorizationFilter -> 로그인 이후를 담당, 이미 발급된 JWT를 검증하고 인증상태를 유지하는 필터")
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final JwtProvider jwtProvider;

    /**
     * JWT 필터를 적용하지 않을 경로 정의
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();

        // Swagger UI 및 API Docs 관련 경로는 필터 건너뛰기
        return path.startsWith("/swagger-ui") ||
               path.startsWith("/v3/api-docs") ||
               path.startsWith("/api-docs") ||
               path.startsWith("/swagger-resources") ||
               path.startsWith("/webjars") ||
               path.equals("/swagger-ui.html") ||
               // H2 Console
               path.startsWith("/h2-console") ||
               // 인증 불필요 경로
               path.equals("/") ||
               path.equals("/login") ||
               path.equals("/join") ||
               path.startsWith("/oauth/");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // request에서 Authorization 헤더를 찾음
        String authorization = request.getHeader("Authorization");
        System.out.println("🔹 Raw Authorization header: [" + authorization + "]");

        // Authorization 헤더 검증
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            System.out.println("token null");
            filterChain.doFilter(request, response);
            // 조건이 해당되면 메소드 종료 (필수)
            return;
        }

        // Bearer 부분 제거 후 순수 토큰만 획득
        String token = authorization.replaceFirst("(?i)^Bearer\\s+", "").trim();
        System.out.println("✅ Extracted Token: [" + token + "]");

        try {
            //JJWT로 검증 (verify 역할)
            jwtUtil.verify(token); // 내부적으로 서명, 만료, 구조 모두 검사함

            //만료 체크 (verify에서 이미 확인되지만, 명시적으로 로그용)
            if (jwtUtil.isExpired(token)) {
                sendUnauthorized(response, "Token expired");
                return;
            }

            // JWT 유효 → Authentication 객체 생성 (Provider에 위임)
            Authentication authentication = jwtProvider.getAuthentication(token);
            // SecurityContext에  사용자 등록
            SecurityContextHolder.getContext().setAuthentication(authentication);

        } catch (ExpiredJwtException e) {
            System.out.println("⛔ JWT 만료: " + e.getMessage());
            sendUnauthorized(response, "Token expired");
            return;
        } catch (JwtException e) {
            System.out.println("⛔ JWT 위조 또는 잘못된 형식: " + e.getMessage());
            sendUnauthorized(response, "Invalid token");
            return;
        } catch (Exception e) {
            System.out.println("⛔ 토큰 검증 중 예상치 못한 오류: " + e.getMessage());
            sendUnauthorized(response, "Token verification failed");
            return;
        }

        filterChain.doFilter(request, response);
    }

    private void sendUnauthorized(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");

        Map<String, String> body = Map.of("error", message);
        new ObjectMapper().writeValue(response.getWriter(), body);
    }

}
