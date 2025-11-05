package com.swyp.wedding.dto.weddinghall;

import com.swyp.wedding.entity.weddinghall.WeddingHall;
import com.swyp.wedding.entity.weddinghall.WeddingHallEnum;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class WeddingHallRequestTest {

    @DisplayName("DTO 기능 테스트 입니다.")
    @Test
    void test() {
        // given
        String testName = "test";
        WeddingHallEnum testVenueType = WeddingHallEnum.WEDDING_HALL;
        int parking = 10;
        String address = "test";
        String phone = "010-1111-1111";
        String email = "test@test";

        WeddingHallRequest request = new WeddingHallRequest();
        request.setName(testName);
        request.setVenueType(testVenueType);
        request.setParking(parking);
        request.setAddress(address);
        request.setPhone(phone);
        request.setEmail(email);

        // when
        WeddingHall weddingHall = request.toEntity();

        // then
        assertThat(weddingHall.getName()).isEqualTo(request.getName());
        assertThat(weddingHall.getPhone()).isEqualTo(request.getPhone());
        assertThat(weddingHall.getAddress()).isEqualTo(request.getAddress());
        assertThat(weddingHall.getParking()).isEqualTo(request.getParking());
        assertThat(weddingHall.getEmail()).isEqualTo(request.getEmail());
        assertThat(weddingHall.getVenueType()).isEqualTo(request.getVenueType());
    }
}