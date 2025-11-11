package com.swyp.wedding.security.jwt;

import com.swyp.wedding.security.user.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import com.swyp.wedding.entity.user.User;
import com.swyp.wedding.entity.user.UserEnum;

@Component
@RequiredArgsConstructor
public class JwtProvider {

    private final JwtUtil jwtUtil;

    public Authentication getAuthentication(String token) {
        String userId = jwtUtil.getUsername(token);
        String role = jwtUtil.getRole(token).replace("ROLE_", "");

        // 임시 User 객체 생성 (DB Access 없이)
        User user = User.builder()
                .userId(userId)
                .password("temp")
                .auth(UserEnum.valueOf(role))
                .build();

        CustomUserDetails details = new CustomUserDetails(user);

        // Spring Security 인증 객체 생성
        return new UsernamePasswordAuthenticationToken(details, null, details.getAuthorities());
    }
}
