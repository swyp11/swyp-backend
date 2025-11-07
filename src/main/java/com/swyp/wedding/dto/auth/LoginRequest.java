package com.swyp.wedding.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(description = "기본 로그인을 위한 request")
public class LoginRequest {

    private String userId;
    private String password;
}
