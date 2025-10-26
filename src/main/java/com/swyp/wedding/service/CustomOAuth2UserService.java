package com.swyp.wedding.service;

import com.swyp.wedding.dto.oAuth2.CustomOAuth2User;
import com.swyp.wedding.dto.oAuth2.GoogleResponse;
import com.swyp.wedding.dto.oAuth2.OAuth2Response;
import com.swyp.wedding.entity.user.*;
import com.swyp.wedding.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(oAuth2userRequest);

        String registrationId = oAuth2userRequest.getClientRegistration().getRegistrationId();

        OAuth2Response oAuth2Response;
        //TODO 이후 google 외에 카카오 or naver 추가 구현
        if (registrationId.equals("google")) {
            oAuth2Response = new GoogleResponse(oAuth2User.getAttributes());
        } else return null;

        //리소스 서버에서 발급 받은 정보로 사용자를 특정할 아이디값을 만듬
        String userid = oAuth2Response.getProvider() + " " + oAuth2Response.getProviderId();

        User user = userRepository.findByUserId(userid) // 이미 로그인한 유저라면 조회만, 아니라면 저장
                .orElseGet(() -> {
                    User newUser = User.builder()
                        .userId(userid)
                        .email(oAuth2Response.getEmail())
                        .name(oAuth2Response.getName())
                        .providerId(oAuth2Response.getProviderId())
                        .auth(UserEnum.USER)
                        .build();

                    return userRepository.save(newUser);
                });

        return new CustomOAuth2User(user); // -> DB에서 가져온 User 엔티티
    }
}
