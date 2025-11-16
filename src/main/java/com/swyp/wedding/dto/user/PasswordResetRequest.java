package com.swyp.wedding.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class PasswordResetRequest {
    @NotBlank(message = "이메일은 필수입니다.")
    @Email(message = "올바른 이메일 형식이 아닙니다.")
    private String email;

    @NotBlank(message = "새 비밀번호는 필수입니다.")
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)[A-Za-z\\d]{8,20}$",
            message = "비밀번호는 문자 + 숫자 8~20자를 충족해야합니다.")
    private String newPassword;

    @NotBlank(message = "이메일 인증 토큰은 필수입니다.")
    private String verificationToken;
}
