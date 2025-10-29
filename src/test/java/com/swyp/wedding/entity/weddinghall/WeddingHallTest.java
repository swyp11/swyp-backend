package com.swyp.wedding.entity.weddinghall;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class WeddingHallTest {

    @DisplayName("웨딩홀 엔티티 생성 테스트를 진행합니다.")
    @Test
    void test() {
        // given
        Long testId = 1L;
        String testName = "test";
        WeddingHallEnum testVenueType = WeddingHallEnum.WEDDING_HALL;
        int testParking = 10;
        String testAddress = "서울시";
        String testPhone = "010-1111-1111";
        String testEmail = "test@test";

        // when
        WeddingHall weddingHall = WeddingHall.builder()
                .id(testId)
                .name(testName)
                .venueType(testVenueType)
                .phone(testPhone)
                .address(testAddress)
                .email(testEmail)
                .parking(testParking)
                .build();

        // then
        assertThat(weddingHall.getId()).isEqualTo(testId);
        assertThat(weddingHall.getName()).isEqualTo(testName);
        assertThat(weddingHall.getVenueType()).isEqualTo(testVenueType);
        assertThat(weddingHall.getPhone()).isEqualTo(testPhone);
        assertThat(weddingHall.getAddress()).isEqualTo(testAddress);
        assertThat(weddingHall.getAddress()).isEqualTo(testAddress);
        assertThat(weddingHall.getEmail()).isEqualTo(testEmail);
    }
}