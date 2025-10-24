package com.swyp.wedding.repository.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import com.swyp.wedding.entity.user.User;
import com.swyp.wedding.entity.user.UserEnum;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @DisplayName("레포지토리 정상 동작 테스트를 진행합니다.")
    @Test
    void saveAndFindTest() {
        // given
        String testId = "testUserId";
        String testPassword = "testPassword";
        String testName = "test";
        LocalDate testBirth = LocalDate.of(2025, 10, 19);
        String testPhoneNumber = "010-1111-1111";
        String testAddress = "서울시 테스트구";
        String testEmail = "test@gmail.com";
        UserEnum testAuth = UserEnum.USER;

        User user = User.builder()
                .userId(testId)
                .password(testPassword)
                .name(testName)
                .birth(testBirth)
                .phoneNumber(testPhoneNumber)
                .address(testAddress)
                .email(testEmail)
                .auth(testAuth)
                .build();
        // when
        userRepository.save(user);

        // then
        User result = userRepository.findById(user.getId()).get();

        assertThat(result).isNotNull();
        assertThat(result.getUserId()).isEqualTo(user.getUserId());
        assertThat(result.getPassword()).isEqualTo(user.getPassword());
        assertThat(result.getName()).isEqualTo(user.getName());
        assertThat(result.getBirth()).isEqualTo(user.getBirth());
        assertThat(result.getPhoneNumber()).isEqualTo(user.getPhoneNumber());
        assertThat(result.getAddress()).isEqualTo(user.getAddress());
        assertThat(result.getEmail()).isEqualTo(user.getEmail());
        assertThat(result.getAuth()).isEqualTo(user.getAuth());
    }
}