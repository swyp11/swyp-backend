package com.swyp.wedding.dto.hall;

import com.swyp.wedding.dto.user.UserRequest;
import com.swyp.wedding.entity.hall.Hall;
import com.swyp.wedding.entity.hall.HallType;
import com.swyp.wedding.entity.hall.LightType;
import com.swyp.wedding.entity.user.User;
import com.swyp.wedding.entity.user.UserEnum;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class HallRequestTest {

    @DisplayName("DTO 기능 테스트 입니다.")
    @Test
    void test() {
        // given
        String name = "루미에르 홀";
        int capacityMin = 80;
        int capacityMax = 220;
        HallType hallType = HallType.SINGLE;
        int floorNo = 2;
        LightType lightType = LightType.BRIGHT;
        BigDecimal areaM2 = new BigDecimal("320.50");
        BigDecimal ceilingHeight = new BigDecimal("6.20");
        boolean stage = true;
        boolean ledWall = false;
        BigDecimal aisleLength = new BigDecimal("18.0");
        boolean pillar = true;
        boolean status = true;
        String description = "자연광 + 샹들리에";

        HallRequest request = new HallRequest();
        request.setName(name);
        request.setCapacityMin(capacityMin);
        request.setCapacityMax(capacityMax);
        request.setHallType(hallType);
        request.setFloorNo(floorNo);
        request.setLightType(lightType);
        request.setAreaM2(areaM2);
        request.setCeilingHeight(ceilingHeight);
        request.setStage(stage);
        request.setLedWall(ledWall);
        request.setAisleLength(aisleLength);
        request.setPillar(pillar);
        request.setStatus(status);
        request.setDescription(description);

        // when
        Hall hall = request.toEntity();

        // then
        assertThat(hall.getName()).isEqualTo(name);
        assertThat(hall.getCapacityMin()).isEqualTo(capacityMin);
        assertThat(hall.getCapacityMax()).isEqualTo(capacityMax);
        assertThat(hall.getHallType()).isEqualTo(hallType);
        assertThat(hall.getFloorNo()).isEqualTo(floorNo);
        assertThat(hall.getLightType()).isEqualTo(lightType);
        assertThat(hall.getAreaM2()).isEqualByComparingTo(areaM2);
        assertThat(hall.getCeilingHeight()).isEqualByComparingTo(ceilingHeight);
        assertThat(hall.isStage()).isEqualTo(stage);
        assertThat(hall.isLedWall()).isEqualTo(ledWall);
        assertThat(hall.getAisleLength()).isEqualByComparingTo(aisleLength);
        assertThat(hall.isPillar()).isEqualTo(pillar);
        assertThat(hall.isStatus()).isEqualTo(status);
        assertThat(hall.getDescription()).isEqualTo(description);

        assertThat(hall.getRegDt()).isNull();
        assertThat(hall.getUpdateDt()).isNull();
    }
}