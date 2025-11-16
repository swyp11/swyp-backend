package com.swyp.wedding.controller.user;

import com.swyp.wedding.dto.auth.EmailAuthRequest;
import com.swyp.wedding.dto.auth.OAuthExtraInfoRequest;
import com.swyp.wedding.dto.user.UserRequest;
import com.swyp.wedding.dto.user.UserResponse;
import com.swyp.wedding.dto.user.UserUpdateRequest;
import com.swyp.wedding.global.response.ApiResponse;
import com.swyp.wedding.entity.user.AuthPurpose;
import com.swyp.wedding.security.user.CustomUserDetails;
import com.swyp.wedding.service.user.AuthCodeService;
import com.swyp.wedding.service.user.JoinService;
import com.swyp.wedding.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "사용자", description = "사용자 관리 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final JoinService joinService;
    private final UserService userService;
    private final AuthCodeService authCodeService;

    // 1. 이메일 인증코드 발송
    @PostMapping("/email-auth")
    @Operation(summary = "이메일 인증코드 발송")
    public ResponseEntity<ApiResponse<String>> sendAuthCode(@RequestParam String email) {
        authCodeService.sendAuthCode(email, AuthPurpose.SIGNUP);
        return ResponseEntity.ok(ApiResponse.success("인증 코드가 이메일로 전송되었습니다."));
    }

    // 2. 이메일 인증코드 검증
    @PostMapping("/email-auth/verify")
    @Operation(summary = "이메일 인증코드 검증")
    public ResponseEntity<ApiResponse<String>> verifyAuthCode(@RequestBody EmailAuthRequest request) {
        boolean verified  = authCodeService.verifyCode(request.getEmail(), request.getCode(), AuthPurpose.SIGNUP);
        if (!verified) {
            return ResponseEntity.badRequest().body(ApiResponse.error("인증코드가 올바르지 않거나 만료되었습니다."));
        }
        authCodeService.markVerified(request.getEmail(), AuthPurpose.SIGNUP);
        return ResponseEntity.ok(ApiResponse.success("이메일 인증이 완료되었습니다."));
    }

    // 3. 회원가입(인증 완료 후 호출)
    @PostMapping("/join")
    @Operation(summary = "회원가입")
    public ResponseEntity<ApiResponse<String>> joinProcess(@RequestBody UserRequest userRequest) {
        joinService.joinProcess(userRequest);
        return ResponseEntity.ok(ApiResponse.success("회원가입이 완료되었습니다."));
    }

    @Schema(description = "OAuth로그인 후 회원가입 당시 필요한 추가 정보 입력 api")
    @PostMapping("/join/oAuth/extra-info")
    @Operation(summary = "OAuth 추가 정보 입력")
    public ResponseEntity<ApiResponse<UserResponse>> OAuthJoinProcess(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                         @RequestBody OAuthExtraInfoRequest request) {
         UserResponse userResponse = joinService.OAuthJoinProcess(userDetails, request);
         return ResponseEntity.ok(ApiResponse.success(userResponse));
    }

    @Operation(summary = "사용자 정보 조회", description = "현재 로그인한 사용자의 정보를 조회합니다.")
    @GetMapping("/info")
    public ResponseEntity<ApiResponse<UserResponse>> getUserInfo(@AuthenticationPrincipal CustomUserDetails userDetails) {
        UserResponse userResponse = userService.getUserInfo(userDetails);
        return ResponseEntity.ok(ApiResponse.success(userResponse));
    }

    @Operation(summary = "사용자 정보 수정", description = "현재 로그인한 사용자의 정보를 수정합니다.")
    @PutMapping("/info")
    public ResponseEntity<ApiResponse<UserResponse>> updateUserInfo(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                        @RequestBody UserUpdateRequest request) {
        UserResponse userResponse = userService.updateUserInfo(userDetails, request);
        return ResponseEntity.ok(ApiResponse.success(userResponse));
    }

}
