package com.swyp.wedding.service.user;

import com.swyp.wedding.dto.user.PasswordUpdateRequest;
import com.swyp.wedding.dto.user.UserResponse;
import com.swyp.wedding.dto.user.UserUpdateRequest;
import com.swyp.wedding.entity.user.User;
import com.swyp.wedding.repository.user.UserRepository;
import com.swyp.wedding.security.user.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Transactional(readOnly = true)
    public UserResponse getUserInfo(CustomUserDetails userDetails) {
        User user = userRepository.findByUserId(userDetails.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        return UserResponse.builder()
                .id(user.getId())
                .userId(user.getUserId())
                .nickname(user.getNickname())
                .birth(user.getBirth())
                .weddingDate(user.getWeddingDate())
                .email(user.getEmail())
                .provider(user.getProvider())
                .providerId(user.getProviderId())
                .auth(user.getAuth())
                .weddingRole(user.getWeddingRole())
                .phoneNumber(user.getPhoneNumber())
                .address(user.getAddress())
                .build();
    }

    @Transactional
    public UserResponse updateUserInfo(CustomUserDetails userDetails, UserUpdateRequest request) {
        User user = userRepository.findByUserId(userDetails.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        user.updateUserInfo(
                request.getNickname(),
                request.getBirth(),
                request.getWeddingDate(),
                request.getEmail(),
                request.getWeddingRole(),
                request.getPhoneNumber(),
                request.getAddress()
        );

        return UserResponse.builder()
                .id(user.getId())
                .userId(user.getUserId())
                .nickname(user.getNickname())
                .birth(user.getBirth())
                .weddingDate(user.getWeddingDate())
                .email(user.getEmail())
                .provider(user.getProvider())
                .providerId(user.getProviderId())
                .auth(user.getAuth())
                .weddingRole(user.getWeddingRole())
                .phoneNumber(user.getPhoneNumber())
                .address(user.getAddress())
                .build();
    }

    @Transactional
    public void updatePassword(CustomUserDetails userDetails, PasswordUpdateRequest request) {
        User user = userRepository.findByUserId(userDetails.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        // 현재 비밀번호 검증
        if (!bCryptPasswordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new IllegalArgumentException("현재 비밀번호가 일치하지 않습니다.");
        }

        // 새 비밀번호 암호화 및 업데이트
        String encodedNewPassword = bCryptPasswordEncoder.encode(request.getNewPassword());
        user.updatePassword(encodedNewPassword);
    }
}
