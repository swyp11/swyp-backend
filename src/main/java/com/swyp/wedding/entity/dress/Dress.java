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

    @Column(name = "dress_color", length = 50)
    private String dressColor;

    @Column(name = "body_shape", length = 50)
    private String bodyShape;

    @Column(name = "dress_price", length = 50)
    private String dressPrice;

    @Column(name = "dress_length", length = 10)
    private String dressLength;

    @Column(name = "dress_season", length = 50)
    private String dressSeason;

    @Column(name = "dress_brand", length = 50)
    private String dressBrand;

    @Column(name = "dress_designer", length = 50)
    private String dressDesigner;

    @Column(name = "dress_type", length = 50)
    private String dressType;

    @Column(name = "dress_neckline", length = 50)
    private String dressNeckline;

    @Column(name = "dress_mood", length = 50)
    private String dressMood;

    @Column(name = "dress_fabrictype", length = 50)
    private String dressFabrictype;

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