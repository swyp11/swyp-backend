package com.swyp.wedding.oauth.client;

import com.swyp.wedding.oauth.userInfo.OAuthUserInfo;

public interface OAuthClient {
    OAuthUserInfo getUserInfo(String code, String redirectUri);
    String getProvider();  // 예: "google", "kakao", "naver"
}
