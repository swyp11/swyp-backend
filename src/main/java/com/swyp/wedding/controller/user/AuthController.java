package com.swyp.wedding.controller.user;


import com.swyp.wedding.dto.auth.LoginRequest;
import com.swyp.wedding.dto.auth.OAuthCodeRequest;
import com.swyp.wedding.dto.auth.TokenResponse;
import com.swyp.wedding.service.user.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/oauth/{provider}")
    @Operation(summary = "OAuth 로그인" ,description = "provider에는 현재 google만 존재 이후 추가 가능성 대비")
    public ResponseEntity<TokenResponse> OAuthLogin(@PathVariable String provider,
                                                     @RequestBody OAuthCodeRequest auth){
        TokenResponse jwtToken = authService.processOAuthLogin(provider, auth.getCode(), auth.getRedirectUri());
        return ResponseEntity.ok()
                .header("Authorization", "Bearer " +jwtToken.getAccessToken())
                .build();
    }

    // 일반 로그인
    @PostMapping("/login")
    public ResponseEntity<?> Login(@RequestBody LoginRequest loginRequest) {
        TokenResponse tokenResponse = authService.login(loginRequest);
        return ResponseEntity.ok()
                .header("Authorization", "Bearer " + tokenResponse.getAccessToken())
                .build();
    }
}
