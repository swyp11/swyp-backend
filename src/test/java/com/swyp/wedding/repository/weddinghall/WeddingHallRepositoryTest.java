package com.swyp.wedding.repository.weddinghall;

import com.swyp.wedding.entity.weddinghall.WeddingHall;
import com.swyp.wedding.entity.weddinghall.WeddingHallEnum;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
public class WeddingHallRepositoryTest {
    @Autowired
    private WeddingHallRepository weddingHallRepository;

    @DisplayName("레포지토리 정상 동작 테스트를 진행합니다.")
    @Test
    void saveAndFindTest() {
        // given
        String testName = "test";
        WeddingHallEnum testVenueType = WeddingHallEnum.WEDDING_HALL;
        int testParking = 10;
        String testAddress = "서울시";
        String testPhone = "010-1111-1111";
        String testEmail = "test@test";

        WeddingHall weddingHall = WeddingHall.builder()
                .name(testName)
                .venueType(testVenueType)
                .phone(testPhone)
                .address(testAddress)
                .email(testEmail)
                .parking(testParking)
                .build();
        // when
        weddingHallRepository.save(weddingHall);

        // then
        WeddingHall result = weddingHallRepository.findById(weddingHall.getId()).get();

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo(weddingHall.getName());
        assertThat(result.getVenueType()).isEqualTo(weddingHall.getVenueType());
        assertThat(result.getPhone()).isEqualTo(weddingHall.getPhone());
        assertThat(result.getAddress()).isEqualTo(weddingHall.getAddress());
        assertThat(result.getEmail()).isEqualTo(weddingHall.getEmail());
        assertThat(result.getParking()).isEqualTo(weddingHall.getParking());
    }
}
