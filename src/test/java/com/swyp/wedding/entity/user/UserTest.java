package com.swyp.wedding.entity.user;

import jakarta.persistence.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.swyp.wedding.entity.user.User;
import com.swyp.wedding.entity.user.UserEnum;

import java.time.LocalDate;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserTest {
    @Test
    @DisplayName("유저 앤티티 생성 테스트를 진행합니다.")
    void createUser() {
        // given
        Long testId = 1L;
        String testName = "test";
        LocalDate testBirth = LocalDate.of(2025, 10, 19);
        String testPhoneNumber = "010-1111-1111";
        String testAddress = "서울시 테스트구";
        String testEmail = "test@gmail.com";
        UserEnum testAuth = UserEnum.USER;

        // when
        User user = User.builder()
                .id(testId)
                .name(testName)
                .birth(testBirth)
                .phoneNumber(testPhoneNumber)
                .address(testAddress)
                .email(testEmail)
                .auth(testAuth)
                .build();

        // then
        assertThat(user.getId()).isEqualTo(testId);
        assertThat(user.getName()).isEqualTo(testName);
        assertThat(user.getBirth()).isEqualTo(testBirth);
        assertThat(user.getPhoneNumber()).isEqualTo(testPhoneNumber);
        assertThat(user.getAddress()).isEqualTo(testAddress);
        assertThat(user.getPhoneNumber()).isEqualTo(testPhoneNumber);
        assertThat(user.getAddress()).isEqualTo(testAddress);
        assertThat(user.getEmail()).isEqualTo(testEmail);
        assertThat(user.getAuth()).isEqualTo(testAuth);
    }
}