package com.swyp.wedding.repository.hall;

import com.swyp.wedding.dto.hall.HallRequest;
import com.swyp.wedding.entity.hall.Hall;
import com.swyp.wedding.entity.hall.HallType;
import com.swyp.wedding.entity.hall.LightType;
import com.swyp.wedding.entity.user.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
public class HallRepositoryTest {

    @Autowired
    private HallRepository hallRepository;

    @DisplayName("레포지토리 정상 동작 테스트를 진행합니다.")
    @Test
    void saveAndFindTest() {
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
        String desc = "자연광 + 샹들리에";

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
        request.setDesc(desc);

        Hall hall = request.toEntity();
        // when
        hallRepository.save(hall);

        // then
        Hall result = hallRepository.findById(hall.getId()).get();
        
        assertThat(result).isNotNull();
        assertThat(result.getId()).isNotNull();
        assertThat(result.getName()).isEqualTo("루미에르 홀");
        assertThat(result.getCapacityMin()).isEqualTo(80);
        assertThat(result.getCapacityMax()).isEqualTo(220);
        assertThat(result.getHallType()).isEqualTo(HallType.SINGLE);
        assertThat(result.getLightType()).isEqualTo(LightType.BRIGHT);
        assertThat(result.getFloorNo()).isEqualTo(2);
        assertThat(result.getAreaM2()).isEqualByComparingTo("320.50");
        assertThat(result.getCeilingHeight()).isEqualByComparingTo("6.20");
        assertThat(result.getAisleLength()).isEqualByComparingTo("18.0");
        assertThat(result.isPillar()).isTrue();
        assertThat(result.isStage()).isTrue();
        assertThat(result.isLedWall()).isFalse();
        assertThat(result.isStatus()).isTrue();
        assertThat(result.getDesc()).isEqualTo("자연광 + 샹들리에");

        assertThat(result.getRegDt()).isNotNull();
        assertThat(result.getUpdateDt()).isNotNull();
    }
}
