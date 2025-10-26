package com.swyp.wedding.security.jwt;

import com.swyp.wedding.entity.user.UserEnum;
import com.swyp.wedding.security.user.CustomUserDetails;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import com.swyp.wedding.entity.user.User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
@RequiredArgsConstructor

public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        //request에서 Authorization 헤더를 찾음
        String authorization= request.getHeader("Authorization");

        //헤더에 없으면 쿠키에서 찾기(OAuth2의 경우 jwt를 쿠키에 저장하기때문에 확인 필요
        if(authorization == null){
             if(request.getCookies() != null){
                 for(Cookie cookie : request.getCookies()){
                     if(cookie.getName().equals("Authorization")) {
                         authorization = "Bearer " + cookie.getValue();
                         System.out.println("✅ 쿠키에서 토큰 발견!");
                         System.out.println("🔹 JWT: " + cookie.getValue());
                         break;
                     }
                 }
             }
        }


        System.out.println("🔹 Raw Authorization header: [" + authorization + "]");
        //Authorization 헤더 검증

        if (authorization == null || !authorization.startsWith("Bearer ")) {
            System.out.println("token null");
            filterChain.doFilter(request, response);
            //조건이 해당되면 메소드 종료 (필수)
            return;
        }

        //Bearer 부분 제거 후 순수 토큰만 획득
        String token = authorization.substring(7).trim();
        System.out.println("✅ Extracted Token: [" + token + "]");

        //토큰 소멸 시간 검증
        if (jwtUtil.isExpired(token)) {
            System.out.println("token expired");
            filterChain.doFilter(request, response);
            //조건이 해당되면 메소드 종료 (필수)
            return;
        }

        //토큰에서 username(= userId)과 role 획득
        String userId = jwtUtil.getUsername(token);
        String role = jwtUtil.getRole(token);
        role = role.replace("ROLE_", "");      // "USER"

        //userEntity를 생성하여 값 set
        User user = User.builder()
                .userId(userId)
                .password("temppassword") //UserDetails 객체가 요구하는 필드를 형식상 채우는 임시 값
                .auth(UserEnum.valueOf(role))
                .build();

        //UserDetails에 회원 정보 객체 담기
        CustomUserDetails customUserDetails = new CustomUserDetails(user);

        //스프링 시큐리티 인증 토큰 생성
        Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
        //세션에 사용자 등록
        SecurityContextHolder.getContext().setAuthentication(authToken);
        filterChain.doFilter(request, response);
    }
}
