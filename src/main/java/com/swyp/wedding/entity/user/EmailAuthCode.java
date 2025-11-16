package com.swyp.wedding.entity.user;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class EmailAuthCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String code;

    @Enumerated(EnumType.STRING)
    private AuthPurpose purpose; // SIGNUP, PASSWORD_RESET 등

    private LocalDateTime expiresAt;

    private boolean verified; // 인증 성공 여부

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expiresAt);
    }

    public void markVerified() {
        this.verified = true;
    }
}
