package com.swyp.wedding.controller.user;

import com.swyp.wedding.dto.auth.OAuthExtraInfoRequest;
import com.swyp.wedding.dto.user.UserRequest;
import com.swyp.wedding.dto.user.UserResponse;
import com.swyp.wedding.dto.user.UserUpdateRequest;
import com.swyp.wedding.global.response.ApiResponse;
import com.swyp.wedding.security.user.CustomUserDetails;
import com.swyp.wedding.service.JoinService;
import com.swyp.wedding.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "User", description = "사용자 관리 API")
@RestController
@RequiredArgsConstructor
public class UserController {

    private final JoinService joinService;
    private final UserService userService;

    @PostMapping("/join")
    @Operation(summary = "회원가입")
    public ResponseEntity<ApiResponse<String>> JoinProcess(@RequestBody UserRequest userRequest) {
        String result = joinService.JoinProcess(userRequest);
        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @Schema(description = "OAuth로그인 후 회원가입 당시 필요한 추가 정보 입력 api")
    @PostMapping("/join/oAuth/extra-info")
    public ResponseEntity<ApiResponse<UserResponse>> OAuthJoinProcess(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                         @RequestBody OAuthExtraInfoRequest request) {
         UserResponse userResponse = joinService.OAuthJoinProcess(userDetails, request);
         return ResponseEntity.ok(ApiResponse.success(userResponse));
    }

    @Operation(summary = "사용자 정보 조회", description = "현재 로그인한 사용자의 정보를 조회합니다.")
    @GetMapping("/user/info")
    public ResponseEntity<ApiResponse<UserResponse>> getUserInfo(@AuthenticationPrincipal CustomUserDetails userDetails) {
        UserResponse userResponse = userService.getUserInfo(userDetails);
        return ResponseEntity.ok(ApiResponse.success(userResponse));
    }

    @Operation(summary = "사용자 정보 수정", description = "현재 로그인한 사용자의 정보를 수정합니다.")
    @PutMapping("/user/info")
    public ResponseEntity<ApiResponse<UserResponse>> updateUserInfo(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                        @RequestBody UserUpdateRequest request) {
        UserResponse userResponse = userService.updateUserInfo(userDetails, request);
        return ResponseEntity.ok(ApiResponse.success(userResponse));
    }

}
