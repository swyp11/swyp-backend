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

    @Column(name = "color", length = 50)
    private String color;

    @Column(name = "shape", length = 50)
    private String shape;

    @Column(name = "price_range", length = 50)
    private String priceRange;

    @Column(name = "length", length = 10)
    private String length;

    @Column(name = "season", length = 50)
    private String season;

    @Column(name = "brand", length = 50)
    private String brand;

    @Column(name = "designer", length = 50)
    private String designer;

    @Column(name = "type", length = 50)
    private String type;

    @Column(name = "neck_line", length = 50)
    private String neckLine;

    @Column(name = "mood", length = 50)
    private String mood;

    @Column(name = "fabric", length = 50)
    private String fabric;

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