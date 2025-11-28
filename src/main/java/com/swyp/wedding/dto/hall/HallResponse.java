package com.swyp.wedding.dto.hall;

import com.swyp.wedding.entity.hall.Hall;
import com.swyp.wedding.entity.hall.HallType;
import com.swyp.wedding.entity.hall.LightType;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class HallResponse {
    private Long id;
    private String name;
    private int capacityMin;
    private int capacityMax;
    private HallType hallType;
    private int floorNo;
    private LightType lightType;
    private BigDecimal areaM2;
    private BigDecimal ceilingHeight;
    private boolean stage;
    private boolean ledWall;
    private BigDecimal aisleLength;
    private boolean pillar;
    private boolean status;
    private String desc;
    private String imageUrl;
    private LocalDateTime regDt;
    private LocalDateTime updateDt;

    public static HallResponse from(Hall hall) {
        HallResponse response = new HallResponse();

        response.setId(hall.getId());
        response.setName(hall.getName());
        response.setCapacityMin(hall.getCapacityMin());
        response.setCapacityMax(hall.getCapacityMax());
        response.setHallType(hall.getHallType());
        response.setFloorNo(hall.getFloorNo());
        response.setLightType(hall.getLightType());
        response.setAreaM2(hall.getAreaM2());
        response.setCeilingHeight(hall.getCeilingHeight());
        response.setStage(hall.isStage());
        response.setLedWall(hall.isLedWall());
        response.setAisleLength(hall.getAisleLength());
        response.setPillar(hall.isPillar());
        response.setStatus(hall.isStatus());
        response.setDesc(hall.getDescription());
        response.setImageUrl(hall.getImageUrl());
        response.setRegDt(hall.getRegDt());
        response.setUpdateDt(hall.getUpdateDt());

        return response;
    }
}
