package com.swyp.wedding.controller.user;

import com.swyp.wedding.dto.auth.OAuthExtraInfoRequest;
import com.swyp.wedding.dto.user.UserRequest;
import com.swyp.wedding.dto.user.UserResponse;
import com.swyp.wedding.security.user.CustomUserDetails;
import com.swyp.wedding.service.JoinService;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequiredArgsConstructor
public class UserController {

    private final JoinService joinService;

    @PostMapping("/join")
    public String JoinProcess(@RequestBody UserRequest userRequest){
        return joinService.JoinProcess(userRequest);
    }


    @Schema(description = "OAuth로그인 후 회원가입 당시 필요한 추가 정보 입력 api")
    @PostMapping("/join/oAuth/extra-info")
    public ResponseEntity<UserResponse> OAuthJoinProcess(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                         @RequestBody OAuthExtraInfoRequest request){
         UserResponse userResponse = joinService.OAuthJoinProcess(userDetails, request);
         return ResponseEntity.ok(userResponse);
    }

}
