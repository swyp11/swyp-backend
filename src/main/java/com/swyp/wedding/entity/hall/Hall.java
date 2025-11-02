package com.swyp.wedding.entity.hall;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@Entity
@Table(name = "tb_hall")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Hall {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Comment("홀 이름")
    private String name;

    @Comment("최소 수용 인원")
    private int capacityMin;

    @Comment("최대 수용 인원")
    private int capacityMax;

    @Comment("홀 유형")
    @Enumerated(EnumType.STRING)
    private HallType hallType;

    @Comment("층 정보")
    private int floorNo;

    @Comment("조명 유형")
    private LightType lightType;

    @Comment("면적")
    private BigDecimal areaM2;

    @Comment("홀 높이")
    private BigDecimal ceilingHeight;

    @Comment("무대 유무")
    private boolean stage = true;

    @Comment("LED 월 유무")
    private boolean ledWall = false;

    @Comment("복도 길이")
    private BigDecimal aisleLength;

    @Comment("기둥 유무")
    private boolean pillar = false;

    @Comment("홀 관리 유무")
    private boolean status = true;

    @Comment("추가 설명")
    private String desc;

    @Comment("등록 일자")
    private LocalDateTime regDt;

    @Comment("업데이트 일자")
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
