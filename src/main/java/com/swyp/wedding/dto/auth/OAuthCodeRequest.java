package com.swyp.wedding.dto.auth;

import lombok.Data;

@Data
public class OAuthCodeRequest {
    private String code;
    private String redirectUri;
}
