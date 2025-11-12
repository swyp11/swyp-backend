package com.swyp.wedding.service;

import com.swyp.wedding.dto.user.UserResponse;
import com.swyp.wedding.dto.user.UserUpdateRequest;
import com.swyp.wedding.entity.user.User;
import com.swyp.wedding.repository.user.UserRepository;
import com.swyp.wedding.security.user.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

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
}
