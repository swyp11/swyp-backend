package com.swyp.wedding.dto.hall;

import com.swyp.wedding.entity.hall.Hall;
import com.swyp.wedding.entity.hall.HallType;
import com.swyp.wedding.entity.hall.LightType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;
import org.hibernate.annotations.Comment;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class HallRequest {
    private Long id;
    private String name;
    private int capacityMin;
    private int capacityMax;
    private HallType hallType;
    private int floorNo;
    private LightType lightType;
    private BigDecimal areaM2;
    private BigDecimal ceilingHeight;
    private boolean stage = true;
    private boolean ledWall = false;
    private BigDecimal aisleLength;
    private boolean pillar = false;
    private boolean status = true;
    private String description;
    private LocalDateTime regDt;
    private LocalDateTime updateDt;

    public Hall toEntity() {
        return Hall.builder()
                .name(name)
                .capacityMin(capacityMin)
                .capacityMax(capacityMax)
                .hallType(hallType)
                .floorNo(floorNo)
                .lightType(lightType)
                .areaM2(areaM2)
                .stage(stage)
                .ceilingHeight(ceilingHeight)
                .ledWall(ledWall)
                .aisleLength(aisleLength)
                .pillar(pillar)
                .status(status)
                .description(description)
                .build();
    }
}
