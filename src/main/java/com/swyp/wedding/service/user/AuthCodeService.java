package com.swyp.wedding.service.user;

import com.swyp.wedding.entity.user.AuthPurpose;
import com.swyp.wedding.entity.user.EmailAuthCode;
import com.swyp.wedding.repository.user.EmailAuthCodeRepository;
import com.swyp.wedding.repository.user.UserRepository;
import com.swyp.wedding.security.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthCodeService {

    private final EmailAuthCodeRepository emailAuthCodeRepository;
    private final UserRepository userRepository;
    private final MailService mailService;
    private final JwtUtil jwtUtil;

    // 인증 코드 전송
    @Transactional
    public void sendAuthCode(String email, AuthPurpose purpose) {
        // 목적에 따른 사용자 존재 여부 검증
        boolean userExists = userRepository.existsByUserId(email);

        if (purpose == AuthPurpose.SIGNUP && userExists) {
            throw new IllegalStateException("이미 가입된 이메일입니다.");
        }

        if (purpose == AuthPurpose.PASSWORD_RESET && !userExists) {
            throw new IllegalStateException("가입되지 않은 이메일입니다.");
        }

        String code = generateCode();

        // 기존 코드 삭제 (중복 방지)
        emailAuthCodeRepository.deleteByEmailAndPurpose(email, purpose);

        // 새 코드 저장
        EmailAuthCode authCode = EmailAuthCode.builder()
                .email(email)
                .code(code)
                .purpose(purpose)
                .expiresAt(LocalDateTime.now().plusMinutes(5))
                .verified(false)
                .build();

        emailAuthCodeRepository.save(authCode);

        String subject = "[위딩 with ing] 이메일 인증 코드";
        String text = "요청하신 인증 코드는 다음과 같습니다.\n\n" + code + "\n\n5분 내로 입력해주세요.";

        mailService.send(email, subject, text);
    }

    // 코드 검증 및 임시 토큰 발급
    @Transactional
    public String verifyCodeAndGenerateToken(String email, String code, AuthPurpose purpose) {
        EmailAuthCode authCode = emailAuthCodeRepository.findByEmailAndPurpose(email, purpose)
                .filter(auth -> !auth.isExpired() && auth.getCode().equals(code))
                .orElseThrow(() -> new IllegalArgumentException("인증코드가 올바르지 않거나 만료되었습니다."));

        // 인증 코드 삭제 (일회성)
        emailAuthCodeRepository.delete(authCode);

        // 임시 토큰 발급 (5분 유효)
        return jwtUtil.createEmailVerificationToken(email, purpose.name());
    }

    private String generateCode() {
        return UUID.randomUUID().toString().substring(0, 8);
    }
}
