package com.swyp.wedding.entity.user;

import com.swyp.wedding.dto.auth.OAuthExtraInfoRequest;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

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

    private String nickname;

    private LocalDate birth; // 생년월일

    private LocalDate weddingDate; // 결혼식 날짜

    private String email;

    private String provider;     // google, kakao...등

    private String providerId;  // 각 플랫폼의 고유 ID (sub, id 등)

    @Enumerated(EnumType.STRING)
    private UserEnum auth;

    @Enumerated(EnumType.STRING)
    private WeddingRole weddingRole; // 신랑 신부


    // 아래 두 컬럼 현재 필요 없음.
    private String phoneNumber;
    private String address;


    public void updateExtraInfo(OAuthExtraInfoRequest extraInfoRequest) {
        this.nickname = extraInfoRequest.getNickname();
        this.birth = getBirth();
        this.weddingDate = getWeddingDate();
        this.weddingRole = getWeddingRole();
    }
}
