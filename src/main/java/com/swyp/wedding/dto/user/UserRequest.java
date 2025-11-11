package com.swyp.wedding.dto.user;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDate;

import com.swyp.wedding.entity.user.User;
import com.swyp.wedding.entity.user.UserEnum;

@Data
public class UserRequest {
    private Long id;
    private String userId;
    private String password;
    private String name;
    private LocalDate birth;
    private String phoneNumber;
    private String address;
    private String email;
    private String provider;
    private UserEnum auth;

    public User toEntity() {
        return User.builder()
                .userId(userId)
                .password(password)
                .name(name)
                .birth(birth)
                .phoneNumber(phoneNumber)
                .address(address)
                .email(email)
                .provider(provider)
                .auth(auth)
                .build();
    }
}
