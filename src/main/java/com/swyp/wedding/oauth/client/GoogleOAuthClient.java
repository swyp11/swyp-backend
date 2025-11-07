package com.swyp.wedding.oauth.client;

import com.swyp.wedding.oauth.userInfo.GoogleUserInfo;
import com.swyp.wedding.oauth.userInfo.OAuthUserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class GoogleOAuthClient implements OAuthClient {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${oauth.google.client-id}") private String clientId;
    @Value("${oauth.google.client-secret}") private String clientSecret;
    @Value("${oauth.google.token-uri}") private String tokenUri;
    @Value("${oauth.google.userinfo-uri}") private String userinfoUri;

    @Override
    public String getProvider() {
        return "google";
    }

    @Override
    public OAuthUserInfo getUserInfo(String code, String redirectUri) {
        // code → access_token 교환
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("code", code);
        params.add("client_id", clientId);
        params.add("client_secret", clientSecret);
        params.add("redirect_uri", redirectUri);
        params.add("grant_type", "authorization_code");

        // accesstoken 만 꺼냄
        Map<String, Object> tokenResponse =
                restTemplate.postForObject(tokenUri, params, Map.class);
        String accessToken = (String) tokenResponse.get("access_token");

        // 사용자 정보 요청
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        Map<String, Object> userInfo =
                restTemplate.exchange(userinfoUri, HttpMethod.GET, entity, Map.class).getBody();

        // 통일된 DTO로 반환
        return new GoogleUserInfo(userInfo);
    }
}
