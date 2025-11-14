package com.swyp.wedding.service;

import com.swyp.wedding.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.swyp.wedding.entity.user.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class PasswordResetService {

    private final UserRepository userRepository;
    private final RedisTemplate<String, Object> redisTemplate;
    private final BCryptPasswordEncoder passwordEncoder;
    private final MailService mailService;

    // 인증 코드 전송
    public void sendResetCode(String email) {
        User user = userRepository.findByUserId(email)
                .orElseThrow(() -> new IllegalArgumentException("해당 이메일로 가입된 계정이 없습니다."));

        String code = generateCode();
        redisTemplate.opsForValue().set("pwReset:" + email, code, 5, TimeUnit.MINUTES);

        mailService.sendAuthCode(email, code);
    }

    //  코드 검증
    public boolean verifyCode(String email, String code) {
        String savedCode = (String) redisTemplate.opsForValue().get("pwReset:" + email);
        return savedCode != null && savedCode.equals(code);
    }

    // 비밀번호 변경
    @Transactional
    public void updatePassword(String email, String newPassword) {
        String savedCode = (String) redisTemplate.opsForValue().get("pwReset:" + email);
        if (savedCode == null) throw new IllegalStateException("인증 코드가 만료되었습니다.");

        User user = userRepository.findByUserId(email) // userId = email
                .orElseThrow(() -> new IllegalArgumentException("해당 이메일 계정을 찾을 수 없습니다."));

        String encodedPassword = passwordEncoder.encode(newPassword);
        user.updatePassword(encodedPassword);
        userRepository.save(user);

        redisTemplate.delete("pwReset:" + email);
    }

    private String generateCode() {
        return UUID.randomUUID().toString().substring(0, 8);
    }
}
