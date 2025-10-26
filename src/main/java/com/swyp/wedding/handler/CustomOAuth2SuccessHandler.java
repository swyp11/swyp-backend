package com.swyp.wedding.handler;

import com.swyp.wedding.dto.oAuth2.CustomOAuth2User;
import com.swyp.wedding.security.jwt.JwtUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;


@Component
@RequiredArgsConstructor
public class CustomOAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final JwtUtil jwtUtil;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException, IOException {

        //OAuth2User
        CustomOAuth2User customUserDetails = (CustomOAuth2User) authentication.getPrincipal();

        String userId = customUserDetails.getUsername();

        //jwt 생성시 role 정보를 넣기 위해 필요
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        String role = auth.getAuthority();

        // jwt 생성
        String token = jwtUtil.createJwt(userId, role, 60*60*60L);
        // 쿠키에 jwt 저장
        response.addCookie(createCookie("Authorization", token));;
        // 로그인 후 리다이렉트
        //response.sendRedirect("http://localhost:3000/"); //-> 프론트로 리다이렉트
        response.sendRedirect("/home");
        System.out.println("✅ CustomOAuth2SuccessHandler의 확인용 Authorization : " + token);

    }

    //jwt 기반 인증이기 때문에 Cookie 직접생성 후 응답해 주어야함 -> 이후 cors 설정 필요
    private Cookie createCookie(String key, String value) {

        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(60*60*60); // 만료 시간 (초 단위)
        //cookie.setSecure(true);   // HTTPS 환경일 때만 전송 (로컬테스트 시 주석)
        cookie.setPath("/");        // 모든 경로에서 접근 가능
        cookie.setHttpOnly(true);   // JS로 접근 불가 (보안)

//        System.out.println("🍪 쿠키 생성 완료!");
//        System.out.println("Name : " + cookie.getName());
//        System.out.println("Value : " + cookie.getValue());
//        System.out.println("Path : " + cookie.getPath());
//        System.out.println("HttpOnly : " + cookie.isHttpOnly());
//        System.out.println("MaxAge : " + cookie.getMaxAge());

        return cookie;
    }

}
