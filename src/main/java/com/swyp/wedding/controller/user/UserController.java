package com.swyp.wedding.controller.user;

import com.swyp.wedding.dto.auth.EmailAuthRequest;
import com.swyp.wedding.dto.auth.EmailVerificationResponse;
import com.swyp.wedding.dto.auth.OAuthExtraInfoRequest;
import com.swyp.wedding.dto.user.PasswordResetRequest;
import com.swyp.wedding.dto.user.UserRequest;
import com.swyp.wedding.dto.user.UserResponse;
import com.swyp.wedding.dto.user.UserUpdateRequest;
import com.swyp.wedding.global.response.ApiResponse;
import com.swyp.wedding.entity.user.AuthPurpose;
import com.swyp.wedding.security.user.CustomUserDetails;
import com.swyp.wedding.service.user.AuthCodeService;
import com.swyp.wedding.service.user.JoinService;
import com.swyp.wedding.service.user.UserService;
import com.swyp.wedding.service.user.PasswordResetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "사용자", description = "사용자 관리 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
@Validated
public class UserController {

    private final JoinService joinService;
    private final UserService userService;
    private final AuthCodeService authCodeService;
    private final PasswordResetService passwordResetService;

    // ==================== 이메일 인증 API ====================

    @PostMapping("/email-auth")
    @Operation(summary = "이메일 인증코드 발송", description = "회원가입 또는 비밀번호 재설정을 위한 이메일 인증코드를 발송합니다.")
    public ResponseEntity<ApiResponse<String>> sendAuthCode(
            @Parameter(description = "인증코드를 받을 이메일 주소")
            @RequestParam @NotBlank @Email String email,
            @Parameter(description = "인증 목적 (SIGNUP: 회원가입, PASSWORD_RESET: 비밀번호 재설정)")
            @RequestParam(defaultValue = "SIGNUP") AuthPurpose purpose) {
        authCodeService.sendAuthCode(email, purpose);
        return ResponseEntity.ok(ApiResponse.success("인증 코드가 이메일로 전송되었습니다."));
    }

    @PostMapping("/email-auth/verify")
    @Operation(summary = "이메일 인증코드 검증", description = "발송된 인증코드를 검증하고 임시 토큰을 발급합니다.")
    public ResponseEntity<ApiResponse<EmailVerificationResponse>> verifyAuthCode(
            @Valid @RequestBody EmailAuthRequest request,
            @Parameter(description = "인증 목적 (SIGNUP: 회원가입, PASSWORD_RESET: 비밀번호 재설정)")
            @RequestParam(defaultValue = "SIGNUP") AuthPurpose purpose) {
        String token = authCodeService.verifyCodeAndGenerateToken(request.getEmail(), request.getCode(), purpose);
        EmailVerificationResponse response = new EmailVerificationResponse(token, "이메일 인증이 완료되었습니다.");
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    // ==================== 회원가입 API ====================

    @PostMapping("/join")
    @Operation(summary = "회원가입", description = "이메일 인증 토큰과 함께 회원가입을 진행합니다.")
    public ResponseEntity<ApiResponse<String>> joinProcess(@Valid @RequestBody UserRequest userRequest) {
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

    // ==================== 비밀번호 재설정 API ====================

    @PatchMapping("/password/reset")
    @Operation(summary = "비밀번호 재설정", description = "이메일 인증 토큰과 함께 새 비밀번호를 설정합니다.")
    public ResponseEntity<ApiResponse<String>> resetPassword(@Valid @RequestBody PasswordResetRequest request) {
        passwordResetService.resetPasswordWithToken(request.getEmail(), request.getNewPassword(), request.getVerificationToken());
        return ResponseEntity.ok(ApiResponse.success("비밀번호가 성공적으로 변경되었습니다."));
    }

    // ==================== 사용자 정보 API ====================

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
