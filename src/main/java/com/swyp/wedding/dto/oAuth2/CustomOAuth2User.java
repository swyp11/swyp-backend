package com.swyp.wedding.dto.oAuth2;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;
import com.swyp.wedding.entity.user.User;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class CustomOAuth2User implements OAuth2User {

    private final User user;

    @Override
    public Map<String, Object> getAttributes() {
        return Map.of();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Enum -> String으로 변환
        String roleName = "ROLE_" + user.getAuth().name();  // 예: ROLE_USER
        return List.of(new SimpleGrantedAuthority(roleName));
    }

    @Override
    public String getName() {
        return user.getName();
    }

    public String getUsername() {
        return user.getUserId(); // 인증용 ID
    }

    // OAuth2Response로 받아오기때문에 현재 필요없음
    public String getEmail() {
        return user.getEmail();
    }
}
