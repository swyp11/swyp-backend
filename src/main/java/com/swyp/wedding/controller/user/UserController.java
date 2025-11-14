package com.swyp.wedding.controller.user;

import com.swyp.wedding.dto.auth.EmailAuthRequest;
import com.swyp.wedding.dto.auth.OAuthExtraInfoRequest;
import com.swyp.wedding.dto.user.UserRequest;
import com.swyp.wedding.dto.user.UserResponse;
import com.swyp.wedding.entity.user.AuthPurpose;
import com.swyp.wedding.security.user.CustomUserDetails;
import com.swyp.wedding.service.user.AuthCodeService;
import com.swyp.wedding.service.user.JoinService;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final JoinService joinService;
    private final AuthCodeService authCodeService;

    // 1. 이메일 인증코드 발송
    @PostMapping("/email-auth")
    public ResponseEntity<String> sendAuthCode(@RequestParam String email) {
        authCodeService.sendAuthCode(email, AuthPurpose.SIGNUP);
        return ResponseEntity.ok("인증 코드가 이메일로 전송되었습니다.");
    }

    // 2. 이메일 인증코드 검증
    @PostMapping("/email-auth/verify")
    public ResponseEntity<String> verifyAuthCode(@RequestBody EmailAuthRequest request) {
        boolean verified  = authCodeService.verifyCode(request.getEmail(), request.getCode(),AuthPurpose.SIGNUP);
        if (!verified) return ResponseEntity.badRequest().body("인증코드가 올바르지 않거나 만료되었습니다.");
        authCodeService.markVerified(request.getEmail(), AuthPurpose.SIGNUP);
        return ResponseEntity.ok("이메일 인증이 완료되었습니다.");
    }


    // 3. 회원가입(인증 완료 후 호출)
    @PostMapping("/join")
    public ResponseEntity<String> joinProcess(@RequestBody UserRequest userRequest){
        joinService.joinProcess(userRequest);
        return ResponseEntity.ok("회원가입이 완료되었습니다.");
    }

    @Schema(description = "OAuth로그인 후 회원가입 당시 필요한 추가 정보 입력 api")
    @PostMapping("/oauth-info")
    public ResponseEntity<UserResponse> oAuthJoinProcess(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                         @RequestBody OAuthExtraInfoRequest request){
         UserResponse userResponse = joinService.OAuthJoinProcess(userDetails, request);
         return ResponseEntity.ok(userResponse);
    }

}
