package com.swyp.wedding.oauth.userInfo;

import lombok.RequiredArgsConstructor;

import java.util.Map;

@RequiredArgsConstructor
public class GoogleUserInfo implements OAuthUserInfo {

    private final Map<String, Object> attribute;

    @Override
    public String getProvider() {
        return "google";
    }

    // 구글은 응답 구조에 따라 "sub" 또는 "id" 둘 중 하나로 옴
    @Override
    public String getProviderId() {
        if (attribute.containsKey("sub")) {
            return attribute.get("sub").toString();
        } else if (attribute.containsKey("id")) {
            return attribute.get("id").toString();
        } else {
            return "unknown";
        }
    }

    @Override
    public String getEmail() {
        return attribute.get("email").toString();
    }

    @Override
    public String getName() {
        return attribute.get("name").toString();
    }
}
