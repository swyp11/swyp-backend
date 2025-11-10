package com.swyp.wedding.service.likes.impl;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.junit.jupiter.MockitoExtension;

import com.swyp.wedding.entity.likes.Likes;
import com.swyp.wedding.entity.likes.LikesType;
import com.swyp.wedding.entity.user.User;
import com.swyp.wedding.entity.user.UserEnum;
import com.swyp.wedding.repository.hall.HallRepository;
import com.swyp.wedding.repository.likes.LikesRepository;
import com.swyp.wedding.repository.user.UserRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("LikesServiceImpl 테스트")
class LikesServiceImplTest {

    @Mock
    private HallRepository hallRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private LikesRepository likesRepository;

    @InjectMocks
    private LikesServiceImpl likesService;

    private User testUser;
    private Likes testLikes;

    @BeforeEach
    void setUp() {
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

    @DisplayName("찜하기 성공 - HALL 카테고리")
    @Test
    void storeLikes_Success_Hall() {
        // given
        String category = "hall";
        Long postId = 1L;
        String userId = "testUser";

        given(userRepository.findByUserId(userId)).willReturn(Optional.of(testUser));
        given(likesRepository.save(any(Likes.class))).willReturn(testLikes);

        // when
        boolean result = likesService.storeLikes(category, postId, userId);

        // then
        assertTrue(result);
        verify(userRepository).findByUserId(userId);
        verify(likesRepository).save(any(Likes.class));
    }

    @DisplayName("찜하기 시스템 성공입니다.(WEDDING_HALL 카테고리)")
    @Test
    void storeLikes_Success_WeddingHall() {
        // given
        String category = "wedding_hall";
        Long postId = 2L;
        String userId = "testUser";

        given(userRepository.findByUserId(userId)).willReturn(Optional.of(testUser));
        given(likesRepository.save(any(Likes.class))).willReturn(testLikes);

        // when
        boolean result = likesService.storeLikes(category, postId, userId);

        // then
        assertTrue(result);
        verify(userRepository).findByUserId(userId);
        verify(likesRepository).save(any(Likes.class));
    }

    @DisplayName("찜하기 시스템 성공입니다.(DRESS 카테고리)")
    @Test
    void storeLikes_Success_Dress() {
        // given
        String category = "dress";
        Long postId = 3L;
        String userId = "testUser";

        given(userRepository.findByUserId(userId)).willReturn(Optional.of(testUser));
        given(likesRepository.save(any(Likes.class))).willReturn(testLikes);

        // when
        boolean result = likesService.storeLikes(category, postId, userId);

        // then
        assertTrue(result);
        verify(userRepository).findByUserId(userId);
        verify(likesRepository).save(any(Likes.class));
    }

    @DisplayName("찜하기 시스템 성공입니다.(SHOP 카테고리)")
    @Test
    void storeLikes_Success_Shop() {
        // given
        String category = "shop";
        Long postId = 4L;
        String userId = "testUser";

        given(userRepository.findByUserId(userId)).willReturn(Optional.of(testUser));
        given(likesRepository.save(any(Likes.class))).willReturn(testLikes);

        // when
        boolean result = likesService.storeLikes(category, postId, userId);

        // then
        assertTrue(result);
        verify(userRepository).findByUserId(userId);
        verify(likesRepository).save(any(Likes.class));
    }

    @DisplayName("지원하지 않는 카테고리로 인한 찜하기 실패입니다.")
    @Test
    void storeLikes_Fail_UnsupportedCategory() {
        // given
        String category = "invalid_category";
        Long postId = 1L;
        String userId = "testUser";

        given(userRepository.findByUserId(userId)).willReturn(Optional.of(testUser));

        // when
        boolean result = likesService.storeLikes(category, postId, userId);

        // then
        assertFalse(result);
        verify(userRepository).findByUserId(userId);
    }

    @DisplayName("데이터베이스 저장 오류로 인한 찜하기 실패입니다.")
    @Test
    void storeLikes_Fail_DatabaseError() {
        // given
        String category = "hall";
        Long postId = 1L;
        String userId = "testUser";

        given(userRepository.findByUserId(userId)).willReturn(Optional.of(testUser));
        given(likesRepository.save(any(Likes.class))).willThrow(new RuntimeException("Database error"));

        // when
        boolean result = likesService.storeLikes(category, postId, userId);

        // then
        assertFalse(result);
        verify(userRepository).findByUserId(userId);
        verify(likesRepository).save(any(Likes.class));
    }

    @DisplayName("찜 목록 제거에 성공합니다.")
    @Test
    void storeDelete_success() {
        // given
        Long id = 1L;

        when(likesRepository.existsById(anyLong())).thenReturn(true);
        doNothing().when(likesRepository).deleteById(id);

        // when
        boolean result = likesService.deleteLikes(id);

        // then
        assertTrue(result);
        verify(likesRepository).existsById(id);
        verify(likesRepository).deleteById(anyLong());
    }

    @DisplayName("존재하지 ID로 인한 찜 목록 제거 실패입니다.")
    @Test
    void storeDelete_Fail_Not_Exist_Id() {
        // given
        Long id = 9999L;

        when(likesRepository.existsById(anyLong())).thenReturn(false);

        // when
        boolean result = likesService.deleteLikes(id);

        // then
        assertFalse(result);
        verify(likesRepository).existsById(id);
    }

    @DisplayName("데이터베이스 저장 오류로 인한 찜 목록 제거 실패입니다.")
    @Test
    void storeDelete_Fail_DatabaseError() {
        // given
        Long id = 1L;

        when(likesRepository.existsById(anyLong())).thenReturn(true);
        doThrow(new RuntimeException("Database error")).when(likesRepository).deleteById(anyLong());

        // when
        boolean result = likesService.deleteLikes(id);

        // then
        assertFalse(result);
        verify(likesRepository).existsById(id);
        verify(likesRepository).deleteById(anyLong());
    }
}