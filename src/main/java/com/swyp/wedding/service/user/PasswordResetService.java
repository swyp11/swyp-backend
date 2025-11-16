package com.swyp.wedding.service.user;

import com.swyp.wedding.entity.user.AuthPurpose;
import com.swyp.wedding.entity.user.User;
import com.swyp.wedding.repository.user.UserRepository;
import com.swyp.wedding.security.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PasswordResetService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    // 토큰 검증 후 비밀번호 변경
    @Transactional
    public void resetPasswordWithToken(String email, String newPassword, String verificationToken) {
        // 1. 이메일 인증 토큰 검증
        if (!jwtUtil.verifyEmailVerificationToken(verificationToken, email, AuthPurpose.PASSWORD_RESET.name())) {
            throw new IllegalStateException("이메일 인증이 완료되지 않았거나 토큰이 만료되었습니다.");
        }

        // 2. 사용자 조회
        User user = userRepository.findByUserId(email)
                .orElseThrow(() -> new IllegalArgumentException("해당 이메일 계정을 찾을 수 없습니다."));

        // 3. 비밀번호 변경
        user.updatePassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

}
