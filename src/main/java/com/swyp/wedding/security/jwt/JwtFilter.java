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

        // 로그인 요청은 JWT 검사 안 함
        String path = request.getRequestURI();
        System.out.println("🔎 Request URI = " + request.getRequestURI());

        // 💡jwt 검증을 받아야 하는 api를 제외한 부분 -> 즉 지금의 user의 경우 jwt 검증을 받아야함.
        // TODO 나중에 API 정할 때 변경하기
        if(!path.startsWith("/api/**") && !path.startsWith("/user") ){
            filterChain.doFilter(request,response);
            return;
        }

        //-> 여기 부터가 jwt token 검증
        //request에서 Authorization 헤더를 찾음
        String authorization= request.getHeader("Authorization");

        System.out.println("JwtFilter에서 헤더에 Authorization이 있는지 확인!");
        System.out.println("JwtFilter header Authorization : " + authorization);
        //헤더에 없으면 쿠키에서 찾기(OAuth2의 경우 jwt를 쿠키에 저장하기때문에 확인 필요
        if(authorization == null){
            System.out.println( "쿠키 전달 : " + request.getCookies());
             if(request.getCookies() != null){
                 for(Cookie cookie : request.getCookies()){
                     System.out.println(cookie.getName() + " = " + cookie.getValue());
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
