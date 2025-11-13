package com.swyp.wedding.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
@Schema(description = "일단 현재는 accessToken 만 뽑아서 사용중이라 다른 게 필요는 없음")
public class TokenResponse {
    private String accessToken;
    private long expiresIn;
    private String tokenType;  // Bearer

    public static TokenResponse of(String accessToken, long expiresInSeconds) {
        return TokenResponse.builder()
                .accessToken(accessToken)
                .tokenType("Bearer")
                .expiresIn(expiresInSeconds)
                .build();
    }
}