package com.swyp.wedding.controller.user;


import com.swyp.wedding.dto.auth.LoginRequest;
import com.swyp.wedding.dto.auth.OAuthCodeRequest;
import com.swyp.wedding.dto.auth.TokenResponse;
import com.swyp.wedding.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/oauth/login/{provider}")
    @Operation(summary = "OAuth 로그인" ,description = "provider에는 현재 google만 존재 이후 추가 가능성 대비")
    public ResponseEntity<TokenResponse> googleLogin(@PathVariable String provider,
                                                     @RequestBody OAuthCodeRequest auth){
        TokenResponse jwtToken = authService.processOAuthLogin(provider, auth.getCode(), auth.getRedirectUri());
        return ResponseEntity.ok(jwtToken);
    }

    // 일반 로그인
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        TokenResponse tokenResponse = authService.login(loginRequest);
        return ResponseEntity.ok()
                .header("Authorization", "Bearer " + tokenResponse.getAccessToken())
                .build();
    }
}
