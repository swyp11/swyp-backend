package com.swyp.wedding.service.user;

import com.swyp.wedding.entity.user.AuthPurpose;
import com.swyp.wedding.entity.user.EmailAuthCode;
import com.swyp.wedding.entity.user.User;
import com.swyp.wedding.repository.user.EmailAuthCodeRepository;
import com.swyp.wedding.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PasswordResetService {

    private final UserRepository userRepository;
    private final EmailAuthCodeRepository emailAuthCodeRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final AuthCodeService authCodeService;

    // 인증 코드 전송
    public void sendResetCode(String email) {
        authCodeService.sendAuthCode(email, AuthPurpose.PASSWORD_RESET);
    }

    //  코드 검증
    public boolean verifyResetCode(String email, String code) {
        return authCodeService.verifyCode(email, code, AuthPurpose.PASSWORD_RESET);
    }

    // 비밀번호 변경
    @Transactional
    public void updatePassword(String email, String newPassword) {
        EmailAuthCode auth = emailAuthCodeRepository.findByEmailAndPurpose(email, AuthPurpose.PASSWORD_RESET)
                .orElseThrow(() -> new IllegalStateException("인증 기록이 없습니다."));

        if (auth.isExpired()) throw new IllegalStateException("인증 코드가 만료되었습니다.");
        if (!auth.isVerified()) throw new IllegalStateException("인증이 완료되지 않았습니다.");

        User user = userRepository.findByUserId(email) // userId = email
                .orElseThrow(() -> new IllegalArgumentException("해당 이메일 계정을 찾을 수 없습니다."));

        user.updatePassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

    }

}
