package com.swyp.wedding.controller.user;


import com.swyp.wedding.dto.auth.LoginRequest;
import com.swyp.wedding.dto.auth.OAuthCodeRequest;
import com.swyp.wedding.dto.auth.TokenResponse;
import com.swyp.wedding.global.response.ApiResponse;
import com.swyp.wedding.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "인증", description = "로그인 및 OAuth 인증 API")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/oauth/login/{provider}")
    @Operation(summary = "OAuth 로그인", description = "provider에는 현재 google만 존재 이후 추가 가능성 대비")
    public ResponseEntity<ApiResponse<TokenResponse>> OAuthLogin(@PathVariable String provider,
                                                     @RequestBody OAuthCodeRequest auth) {
        TokenResponse jwtToken = authService.processOAuthLogin(provider, auth.getCode(), auth.getRedirectUri());
        return ResponseEntity.ok()
                .header("Authorization", "Bearer " + jwtToken.getAccessToken())
                .body(ApiResponse.success(jwtToken));
    }

    @PostMapping("/login")
    @Operation(summary = "일반 로그인")
    public ResponseEntity<ApiResponse<TokenResponse>> Login(@RequestBody LoginRequest loginRequest) {
        TokenResponse tokenResponse = authService.login(loginRequest);
        return ResponseEntity.ok()
                .header("Authorization", "Bearer " + tokenResponse.getAccessToken())
                .body(ApiResponse.success(tokenResponse));
    }
}
