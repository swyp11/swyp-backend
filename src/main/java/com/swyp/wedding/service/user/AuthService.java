package com.swyp.wedding.service.user;

import com.swyp.wedding.dto.auth.LoginRequest;
import com.swyp.wedding.oauth.userInfo.OAuthUserInfo;
import com.swyp.wedding.dto.auth.TokenResponse;
import com.swyp.wedding.entity.user.UserEnum;
import com.swyp.wedding.oauth.client.OAuthClient;
import com.swyp.wedding.repository.user.UserRepository;
import com.swyp.wedding.security.jwt.JwtUtil;
import com.swyp.wedding.entity.user.User;
import com.swyp.wedding.security.user.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final List<OAuthClient> oAuthClients; // 모든 OAuthClient 구현체 자동 주입
    private final JwtUtil jwtUtil;

    // OAuth 로그인
    public TokenResponse processOAuthLogin(String provider, String code, String redirectUri) {
        // 1️. provider 이름으로 알맞은 OAuthClient 선택
        OAuthClient oAuthClient = oAuthClients.stream()
                .filter(client -> client.getProvider().equalsIgnoreCase(provider))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("지원하지 않는 provider: " + provider));

        // 2. 공통 인터페이스로 사용자 정보 가져오기
        OAuthUserInfo userInfo = oAuthClient.getUserInfo(code, redirectUri);

        // 콘솔 확인 용
//        System.out.println("✅ Provider: " + userInfo.getProvider());
//        System.out.println("✅ ProviderId: " + userInfo.getProviderId());
//        System.out.println("✅ Email: " + userInfo.getEmail());
//        System.out.println("✅ Name: " + userInfo.getName());

        // 3. DB 조회 또는 신규 회원 등록 -
        User user = userRepository.findByProviderAndProviderId(userInfo.getProvider(), userInfo.getProviderId())
                .orElseGet(()-> registerNewUser(userInfo));

        // 4️. JWT 발급 후 반환
        String accessToken = jwtUtil.createToken(user.getUserId(), user.getAuth().name());
        return TokenResponse.of(accessToken);
    }

    // 신규 회원 등록
    private User registerNewUser(OAuthUserInfo userInfo) {
        String generatedUserId = userInfo.getProvider() + "_" + userInfo.getProviderId();

        User newUser = User.builder()
                .userId(generatedUserId)
                .email(userInfo.getEmail())
                .auth(UserEnum.USER)
                .provider(userInfo.getProvider())
                .providerId(userInfo.getProviderId())
                .build();

        return userRepository.save(newUser);
    }


    public TokenResponse login(LoginRequest loginRequest) {

        try {
            // 1. 사용자 검증 시도 (Spring Security 인증 매니저)
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUserId(),
                            loginRequest.getPassword()
                    )
            );

            // 2. 인증 성공 → UserDetails 꺼내기
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            String username = userDetails.getUsername();
            String role = userDetails.getAuthorities().stream()
                    .findFirst()
                    .map(GrantedAuthority::getAuthority)
                    .orElse("ROLE_USER");

            // 3. JWT 토큰 생성
            String token = jwtUtil.createToken(username, role);

            // 4. 응답 객체 반환
            return TokenResponse.of(token);
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("잘못된 아이디/비밀번호입니다.");
        } catch (UsernameNotFoundException e) {
            throw new UsernameNotFoundException("존재하지 않는 사용자입니다.");
        }
    }
}
