package com.swyp.wedding.dto.user;

import com.swyp.wedding.entity.user.WeddingRole;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;

@Data
@Schema(description = "사용자 정보 수정 요청 DTO")
public class UserUpdateRequest {

    @Schema(description = "닉네임", example = "홍길동")
    private String nickname;

    @Schema(description = "생년월일", example = "1990-01-01")
    private LocalDate birth;

    @Schema(description = "결혼식 날짜", example = "2024-12-25")
    private LocalDate weddingDate;

    @Schema(description = "이메일", example = "user@example.com")
    private String email;

    @Schema(description = "결혼 역할 (신랑/신부)", example = "GROOM")
    private WeddingRole weddingRole;

    // 현재 미사용이지만 향후 사용 가능
    @Schema(description = "전화번호", example = "010-1234-5678")
    private String phoneNumber;

    @Schema(description = "주소", example = "서울시 강남구")
    private String address;
}
