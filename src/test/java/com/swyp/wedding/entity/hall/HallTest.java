package com.swyp.wedding.entity.hall;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class HallTest {

    @DisplayName("홀 기본적인 엔티티 테스트입니다.")
    @Test
    void test() {
        // given
        Hall hall = new Hall();

        // when
        ReflectionTestUtils.setField(hall, "name", "루미에르 홀");
        ReflectionTestUtils.setField(hall, "capacityMin", 80);
        ReflectionTestUtils.setField(hall, "capacityMax", 220);
        ReflectionTestUtils.setField(hall, "hallType", HallType.SINGLE);
        ReflectionTestUtils.setField(hall, "lightType", LightType.BRIGHT);
        ReflectionTestUtils.setField(hall, "floorNo", 2);
        ReflectionTestUtils.setField(hall, "areaM2", new BigDecimal("320.50"));
        ReflectionTestUtils.setField(hall, "ceilingHeight", new BigDecimal("6.2"));
        ReflectionTestUtils.setField(hall, "aisleLength", new BigDecimal("18.0"));
        ReflectionTestUtils.setField(hall, "pillar", true);
        ReflectionTestUtils.setField(hall, "desc", "창가 자연광, 대형 샹들리에");

        // then
        assertThat(hall.getName()).isEqualTo("루미에르 홀");
        assertThat(hall.getCapacityMin()).isEqualTo(80);
        assertThat(hall.getCapacityMax()).isEqualTo(220);
        assertThat(hall.getHallType()).isEqualTo(HallType.SINGLE);
        assertThat(hall.getLightType()).isEqualTo(LightType.BRIGHT);
        assertThat(hall.getFloorNo()).isEqualTo(2);
        assertThat(hall.getAreaM2()).isEqualByComparingTo("320.50");
        assertThat(hall.getCeilingHeight()).isEqualByComparingTo("6.2");
        assertThat(hall.isPillar()).isTrue();
        assertThat(hall.isStage()).isTrue();
        assertThat(hall.isLedWall()).isFalse();
        assertThat(hall.isStatus()).isTrue();
        assertThat(hall.getDescription()).isEqualTo("창가 자연광, 대형 샹들리에");
    }
}