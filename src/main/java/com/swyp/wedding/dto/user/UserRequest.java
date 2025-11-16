package com.swyp.wedding.dto.user;

import com.swyp.wedding.entity.user.User;
import com.swyp.wedding.entity.user.UserEnum;
import com.swyp.wedding.entity.user.WeddingRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UserRequest {
    private Long id;

    @NotBlank(message = "이메일은 필수입니다.")
    @Email(message = "올바른 이메일 형식이 아닙니다.")
    private String userId; // = email

    @NotBlank(message = "비밀번호는 필수입니다.")
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)[A-Za-z\\d]{8,20}$",
            message = "비밀번호는 문자 + 숫자 8~20자를 충족해야합니다.")
    private String password;

    @NotBlank(message = "닉네임은 필수입니다.")
    private String nickname;

    @NotNull(message = "생년월일은 필수입니다.")
    private LocalDate birth;

    private LocalDate weddingDate;
    private String provider;
    private String providerId;
    private UserEnum auth;

    @NotNull(message = "웨딩 역할은 필수입니다.")
    private WeddingRole weddingRole;

    @NotBlank(message = "이메일 인증 토큰은 필수입니다.")
    private String verificationToken;

    // 현재 미사용
//    private String phoneNumber;
//    private String address;

    public User toEntity() {
        return User.builder()
                .userId(userId)
                .password(password)
                .nickname(nickname)
                .birth(birth)
                .weddingDate(weddingDate)
                .provider(provider)
                .providerId(providerId)
                .auth(auth)
                .weddingRole(weddingRole)
                .build();
    }
}
