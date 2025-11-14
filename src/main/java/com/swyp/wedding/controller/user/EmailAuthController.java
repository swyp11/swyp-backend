package com.swyp.wedding.controller.user;

import com.swyp.wedding.dto.auth.EmailAuthRequest;
import com.swyp.wedding.service.user.EmailAuthService;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/email")
@Schema(description = "이메일 인증 관련 controller")
public class EmailAuthController {

    private final EmailAuthService emailAuthService;

    // 이메일 인증코드 발송
    @PostMapping("/send")
    public ResponseEntity<String> sendAuthCode(@RequestBody EmailAuthRequest request) {
        emailAuthService.sendAuthCode(request.getEmail());
        return ResponseEntity.ok("인증 코드가 이메일로 전송되었습니다.");
    }

    //  이메일 인증코드 검증
    @PostMapping("/verify")
    public ResponseEntity<String> verifyAuthCode(@RequestBody EmailAuthRequest request) {
        boolean result = emailAuthService.verifyCode(request.getEmail(), request.getCode());
        if (result) {
            return ResponseEntity.ok("이메일 인증 성공");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("이메일 인증 실패");
        }
    }

}
