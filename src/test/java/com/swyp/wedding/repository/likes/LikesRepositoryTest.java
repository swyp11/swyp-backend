package com.swyp.wedding.repository.likes;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import com.swyp.wedding.dto.hall.HallRequest;
import com.swyp.wedding.entity.hall.Hall;
import com.swyp.wedding.entity.hall.HallType;
import com.swyp.wedding.entity.hall.LightType;
import com.swyp.wedding.entity.likes.Likes;
import com.swyp.wedding.entity.likes.LikesType;
import com.swyp.wedding.entity.user.User;
import com.swyp.wedding.entity.user.UserEnum;

@DataJpaTest
@ActiveProfiles("test")
public class LikesRepositoryTest {
    @Autowired
    private LikesRepository likesRepository;

    private User testUser;
    private Likes testLikes;

    @BeforeEach
    void setup() {
        testUser = User.builder()
                .userId("testUser")
                .name("테스트 사용자")
                .email("test@example.com")
                .password("password")
                .auth(UserEnum.USER)
                .birth(LocalDate.of(1990, 1, 1))
                .address("서울시")
                .phoneNumber("010-1234-5678")
                .provider("local")
                .build();

        testLikes = Likes.builder()
                .user(testUser)
                .likesType(LikesType.HALL)
                .targetId(1L)
                .build();
    }

    @DisplayName("레포지토리 저장 테스트를 진행합니다.")
    @Test
    void saveTest() {
        // when
        likesRepository.save(testLikes);

        // then
        Likes result = likesRepository.findById(testLikes.getId()).get();

        assertThat(result).isNotNull();
        assertThat(result.getUser().getUserId()).isEqualTo("testUser");
        assertThat(result.getLikesType()).isEqualTo(LikesType.HALL);
        assertThat(result.getTargetId()).isEqualTo(1L);
        assertThat(result.getUpdateDt()).isNotNull();
    }

    @DisplayName("데이터 삭제 테스트를 진행합니다.")
    @Test
    void deleteTest() {
        // when
        likesRepository.save(testLikes);
        likesRepository.deleteById(testLikes.getId());

        // then
        assertThat(likesRepository.findById(testLikes.getId())).isEmpty();

    }
}
