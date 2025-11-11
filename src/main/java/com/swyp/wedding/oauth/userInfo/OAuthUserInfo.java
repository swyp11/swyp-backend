package com.swyp.wedding.oauth.userInfo;

public interface OAuthUserInfo {

    String getProvider();
    String getProviderId();
    String getEmail();
    String getName();
}
