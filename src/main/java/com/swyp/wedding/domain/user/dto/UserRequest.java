package com.swyp.wedding.domain.user.dto;

import com.swyp.wedding.domain.user.entity.User;
import com.swyp.wedding.domain.user.entity.UserEnum;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDate;

@Data
public class UserRequest {
    private Long id;
    private String name;
    private LocalDate birth;
    private String phoneNumber;
    private String address;
    private String email;
    private UserEnum auth;

    public User toEntity() {
        return User.builder()
                .name(name)
                .birth(birth)
                .phoneNumber(phoneNumber)
                .address(address)
                .email(email)
                .auth(auth)
                .build();
    }
}
