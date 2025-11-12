package com.swyp.wedding.dto.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.swyp.wedding.dto.user.UserRequest;
import com.swyp.wedding.entity.user.User;
import com.swyp.wedding.entity.user.UserEnum;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class UserRequestTest {

    @DisplayName("DTO 기능 테스트 입니다.")
    @Test
    void test() {
        // given
        String testId = "testUserId";
        String testPassword = "testPassword";
        String testName = "test";
        LocalDate testBirth = LocalDate.of(2025, 10, 19);
        String testPhoneNumber = "010-1111-1111";
        String testAddress = "서울시 테스트구";
        String testEmail = "test@gmail.com";
        UserEnum testAuth = UserEnum.USER;

        UserRequest request = new UserRequest();
        request.setUserId(testId);
        request.setPassword(testPassword);
        request.setName(testName);
        request.setBirth(testBirth);
        request.setPhoneNumber(testPhoneNumber);
        request.setAddress(testAddress);
        request.setEmail(testEmail);
        request.setAuth(testAuth);

        // when
        User user = request.toEntity();

        // then
        assertThat(user.getUserId()).isEqualTo(testId);
        assertThat(user.getPassword()).isEqualTo(testPassword);
        assertThat(user.getName()).isEqualTo(request.getName());
        assertThat(user.getBirth()).isEqualTo(request.getBirth());
        assertThat(user.getPhoneNumber()).isEqualTo(request.getPhoneNumber());
        assertThat(user.getAddress()).isEqualTo(request.getAddress());
        assertThat(user.getAddress()).isEqualTo(request.getAddress());
        assertThat(user.getEmail()).isEqualTo(request.getEmail());
        assertThat(user.getAuth()).isEqualTo(request.getAuth());
    }
}