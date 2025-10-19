package com.swyp.wedding.domain.user.dto;

import com.swyp.wedding.domain.user.entity.User;
import com.swyp.wedding.domain.user.entity.UserEnum;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class UserRequestTest {

    @DisplayName("DTO 기능 테스트 입니다.")
    @Test
    void test() {
        // given
        String testName = "test";
        LocalDate testBirth = LocalDate.of(2025, 10, 19);
        String testPhoneNumber = "010-1111-1111";
        String testAddress = "서울시 테스트구";
        String testEmail = "test@gmail.com";
        UserEnum testAuth = UserEnum.USER;

        UserRequest request = new UserRequest();
        request.setName(testName);
        request.setBirth(testBirth);
        request.setPhoneNumber(testPhoneNumber);
        request.setAddress(testAddress);
        request.setEmail(testEmail);
        request.setAuth(testAuth);

        // when
        User user = request.toEntity();

        // then
        assertThat(user.getName()).isEqualTo(request.getName());
        assertThat(user.getBirth()).isEqualTo(request.getBirth());
        assertThat(user.getPhoneNumber()).isEqualTo(request.getPhoneNumber());
        assertThat(user.getAddress()).isEqualTo(request.getAddress());
        assertThat(user.getAddress()).isEqualTo(request.getAddress());
        assertThat(user.getEmail()).isEqualTo(request.getEmail());
        assertThat(user.getAuth()).isEqualTo(request.getAuth());
    }
}