package com.swyp.wedding.entity.makeupshop;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "tb_makeup_shop")
@Builder
@AllArgsConstructor
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MakeupShop {

    @Id  // Primary Key
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "shop_name", length = 100, nullable = false)
    private String shopName;  // 메이크업샵 이름

    @Column(name = "description", length = 1000)
    private String description;  // 샵 설명

    @Column(name = "address", length = 200)
    private String address;  // 주소

    @Column(name = "phone", length = 50)
    private String phone;  // 연락처 (전화번호, 카카오톡, 기타 연락 가능한 방법)

    @Column(name = "sns_url", length = 500)
    private String snsUrl;  // SNS URL (웹사이트, 인스타그램 등)

    @Column(name = "image_url", length = 500)
    private String imageUrl;  // image URL

    @Column(name = "specialty", length = 200)
    private String specialty;  // 전문분야 (예: 본식메이크업, 야외촬영, 스몰웨딩 등)

    @Column(name = "features", length = 1000)
    private String features;  // 특징 또는 기타 정보

    @Column(name = "reg_dt")
    private LocalDateTime regDt;

    @Column(name = "update_dt")
    private LocalDateTime updateDt;

    // 생성 시 자동으로 시간 설정
    @PrePersist
    protected void onCreate() {
        if (regDt == null) {
            regDt = LocalDateTime.now();
        }
        if (updateDt == null) {
            updateDt = LocalDateTime.now();
        }
    }

    // 업데이트 시 자동으로 시간 갱신
    @PreUpdate
    protected void onUpdate() {
        updateDt = LocalDateTime.now();
    }
}
