package com.swyp.wedding.dto.user;

import com.swyp.wedding.entity.user.UserEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
@AllArgsConstructor
public class UserResponse {
    private Long id;
    private String userId;
    private String password;
    private String nickname;
    private LocalDate birth;
    private LocalDate weddingDate;
    private String email;
    private String provider;
    private String providerId;
    private UserEnum auth;
    private String weddingRole;

    // 현재 미사용
    private String phoneNumber;
    private String address;
}
