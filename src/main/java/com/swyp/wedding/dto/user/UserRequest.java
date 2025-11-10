package com.swyp.wedding.dto.user;

import com.swyp.wedding.entity.user.User;
import com.swyp.wedding.entity.user.UserEnum;
import com.swyp.wedding.entity.user.WeddingRole;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UserRequest {
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
    private WeddingRole weddingRole;

    // 현재 미사용
    private String phoneNumber;
    private String address;

    public User toEntity() {
        return User.builder()
                .userId(userId)
                .password(password)
                .nickname(nickname)
                .birth(birth)
                .weddingDate(weddingDate)
                .phoneNumber(phoneNumber)
                .address(address)
                .email(email)
                .provider(provider)
                .providerId(providerId)
                .auth(auth)
                .weddingRole(weddingRole)
                .build();
    }
}
