package com.swyp.wedding.service.user;

import com.swyp.wedding.entity.user.AuthPurpose;
import com.swyp.wedding.entity.user.EmailAuthCode;
import com.swyp.wedding.repository.user.EmailAuthCodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthCodeService {

    private final EmailAuthCodeRepository emailAuthCodeRepository;
    private final MailService mailService;

    // 인증 코드 전송
    @Transactional
    public void sendAuthCode(String email, AuthPurpose purpose) {
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

        mailService.send(email,subject,text);
    }

    // 코드 검증
    @Transactional(readOnly = true)
    public boolean verifyCode(String email, String code, AuthPurpose purpose) {
        return emailAuthCodeRepository.findByEmailAndPurpose(email, purpose)
                .filter(auth -> !auth.isExpired() && auth.getCode().equals(code))
                .isPresent();
    }

    //  인증 완료 처리 (검증 성공 시 호출)
    @Transactional
    public void markVerified(String email, AuthPurpose purpose) {
        emailAuthCodeRepository.findByEmailAndPurpose(email, purpose)
                .ifPresent(auth -> auth.markVerified());
    }

    private String generateCode() {
        return UUID.randomUUID().toString().substring(0, 8);
    }
}
