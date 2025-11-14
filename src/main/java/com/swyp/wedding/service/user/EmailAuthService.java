package com.swyp.wedding.service.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Schema(description = "이메일 인증 코드 저장소 관련 코드")
public class EmailAuthService {
    private final RedisTemplate<String, String> redisTemplate;
    private final MailService mailService;

    // 인증 코드 생성
    public String generateCode() {
        return UUID.randomUUID().toString().substring(0, 8); // 8자리 랜덤 코드
    }

    // 인증 코드 발송 및 redis 저장
    public void sendAuthCode(String email) {
        String code = generateCode();
        redisTemplate.opsForValue().set("emailAuth:" + email, code, 5, TimeUnit.MINUTES);
        mailService.sendAuthCode(email, code);
    }

    // 사용자가 입력한 코드 검증
    public boolean verifyCode(String email, String code) {
        String saved = redisTemplate.opsForValue().get("emailAuth:" + email);
        return saved != null && saved.equals(code);
    }


}
