package com.swyp.wedding.controller.user;

import com.swyp.wedding.dto.auth.OAuthExtraInfoRequest;
import com.swyp.wedding.dto.user.UserRequest;
import com.swyp.wedding.dto.user.UserResponse;
import com.swyp.wedding.security.user.CustomUserDetails;
import com.swyp.wedding.service.user.JoinService;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/join")
public class UserController {

    private final JoinService joinService;

    @PostMapping
    public ResponseEntity<String> JoinProcess(@RequestBody UserRequest userRequest){
        try {
            joinService.joinProcess(userRequest);
            return ResponseEntity.ok("회원가입이 완료되었습니다.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }


    @Schema(description = "OAuth로그인 후 회원가입 당시 필요한 추가 정보 입력 api")
    @PostMapping("/oAuth/extra-info")
    public ResponseEntity<UserResponse> OAuthJoinProcess(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                         @RequestBody OAuthExtraInfoRequest request){
         UserResponse userResponse = joinService.OAuthJoinProcess(userDetails, request);
         return ResponseEntity.ok(userResponse);
    }

}
