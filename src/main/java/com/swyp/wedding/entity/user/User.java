package com.swyp.wedding.entity.user;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name = "tb_user")
@Builder
@AllArgsConstructor
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userId; //내부 로그인용 ID (일반회원: 직접입력 / 소셜회원: provider_providerId)

    private String password;

    private String name;

    private LocalDate birth;

    private String phoneNumber;

    private String address;

    private String email;

    private String provider;     // google, kakao...등

    private String providerId;  // 각 플랫폼의 고유 ID (sub, id 등)

    @Enumerated(EnumType.STRING)
    private UserEnum auth;
}
