package com.swyp.wedding.controller.user;

import com.swyp.wedding.service.PasswordResetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/password")
@RequiredArgsConstructor

public class PasswordResetController {

    private final PasswordResetService passwordResetService;

    // 1. 비밀번호 초기화 요청 - 찾기를 위해 인증 코드 발송 (인증 코드 전송)
    @PostMapping("/reset/request")
    public ResponseEntity<String> requestReset(@RequestParam String email) {
        passwordResetService.sendResetCode(email);
        return ResponseEntity.ok("비밀번호 재설정 코드가 이메일로 발송되었습니다.");
    }

    // 2. 코드 검증
    @PostMapping("/reset/verify")
    public ResponseEntity<String> verifyCode(@RequestParam String email, @RequestParam String code) {
        boolean verified = passwordResetService.verifyCode(email, code);
        if (!verified) return ResponseEntity.badRequest().body("인증 코드가 올바르지 않습니다.");
        return ResponseEntity.ok("인증 성공");
    }

    // 3. 새 비밀번호 설정
    @PostMapping("/reset/confirm")
    public ResponseEntity<String> confirmReset(@RequestParam String email, @RequestParam String newPassword) {
        passwordResetService.updatePassword(email, newPassword);
        return ResponseEntity.ok("비밀번호가 성공적으로 변경되었습니다.");
    }
}
