package com.swyp.wedding.service;

import com.swyp.wedding.oauth.userInfo.OAuthUserInfo;
import com.swyp.wedding.dto.auth.TokenResponse;
import com.swyp.wedding.entity.user.UserEnum;
import com.swyp.wedding.oauth.client.OAuthClient;
import com.swyp.wedding.repository.user.UserRepository;
import com.swyp.wedding.security.jwt.JwtUtil;
import com.swyp.wedding.entity.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthService{

    private final UserRepository userRepository;
    private final List<OAuthClient> oAuthClients; // 모든 OAuthClient 구현체 자동 주입
    private final JwtUtil jwtUtil;

    public TokenResponse processOAuthLogin(String provider, String code, String redirectUri){
        // 1️. provider 이름으로 알맞은 OAuthClient 선택
        OAuthClient oAuthClient = oAuthClients.stream()
                .filter(client -> client.getProvider().equalsIgnoreCase(provider))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("지원하지 않는 provider: " + provider));

        // 2. 공통 인터페이스로 사용자 정보 가져오기
        OAuthUserInfo userInfo = oAuthClient.getUserInfo(code, redirectUri);

        // 3. DB 조회 또는 신규 회원 등록
        User user = userRepository.findByEmail(userInfo.getEmail())
                .orElseGet(() -> registerNewUser(userInfo, provider));

        // 4️. JWT 발급 후 반환
        String accessToken = jwtUtil.createToken(user.getUserId(), user.getAuth().name());
        return new TokenResponse(accessToken);
    }

    // 신규 회원 등록
    private User registerNewUser(OAuthUserInfo userInfo, String provider) {
        User newUser = User.builder()
                .email(userInfo.getEmail())
                .name(userInfo.getName())
                .auth(UserEnum.USER)
                .provider(provider)
                .build();

        return userRepository.save(newUser);
    }

}
