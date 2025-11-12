package com.swyp.wedding.entity.dress;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "tb_dress")
@Builder
@AllArgsConstructor
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Dress {

    @Id  // Primary Key
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", length = 100)
    private String name; 

    @Column(name = "color", length = 50)
    private String color;

    @Column(name = "shape", length = 50)
    private String shape;

    @Column(name = "price_range", length = 50)
    private String priceRange;

    @Enumerated(EnumType.STRING)
    @Column(name = "length", length = 10)
    private DressEnum.Length length;

    @Enumerated(EnumType.STRING)
    @Column(name = "season", length = 50)
    private DressEnum.Season season;

    @Column(name = "shop_name", length = 100, nullable = false)
    private String shopName;  // DressShop의 shopName과 연결

    @Column(name = "designer", length = 50)
    private String designer;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", length = 50)
    private DressEnum.Type type;

    @Enumerated(EnumType.STRING)
    @Column(name = "neck_line", length = 50)
    private DressEnum.Neckline neckLine;

    @Enumerated(EnumType.STRING)
    @Column(name = "mood", length = 50)
    private DressEnum.Mood mood;

    @Column(name = "fabric", length = 200)  // 여러 원단 타입을 콤마로 구분해서 저장
    private String fabric;

    @Column(name = "image_url", length = 500)
    private String imageUrl;  // image URL

    @Column(name = "features", length = 500)
    private String features;  // 특징 또는 기타 컬럼

    @Column(name = "reg_dt")
    private LocalDateTime regDt;

    @Column(name = "update_dt")
    private LocalDateTime updateDt;

    // 생성 시 자동으로 시간 설정
    @PrePersist
    protected void onCreate() {
        regDt = LocalDateTime.now();
        updateDt = LocalDateTime.now();
    }

    // 업데이트 시 자동으로 시간 갱신
    @PreUpdate
    protected void onUpdate() {
        updateDt = LocalDateTime.now();
    }
}