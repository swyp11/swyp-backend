package com.swyp.wedding.service.weddinghall;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.junit.jupiter.MockitoExtension;

import com.swyp.wedding.dto.weddinghall.WeddingHallRequest;
import com.swyp.wedding.dto.weddinghall.WeddingHallResponse;
import com.swyp.wedding.entity.weddinghall.WeddingHall;
import com.swyp.wedding.entity.weddinghall.WeddingHallEnum;
import com.swyp.wedding.repository.weddinghall.WeddingHallRepository;
import org.springframework.dao.DataIntegrityViolationException;

@ExtendWith(MockitoExtension.class)  // Mock 사용을 위한 확장
@DisplayName("WeddingHallService 단위 테스트")
class WeddingHallServiceImplTest {

    @Mock
    private WeddingHallRepository weddingHallRepository;

    @InjectMocks
    private WeddingHallServiceImpl weddingHallService;

    private WeddingHall testWeddingHall1;
    private WeddingHall testWeddingHall2;

    @BeforeEach
    void setUp() {
        testWeddingHall1 = WeddingHall.builder()
                .id(1L)
                .name("그랜드 웨딩홀")
                .venueType(WeddingHallEnum.WEDDING_HALL)
                .parking(100)
                .address("서울시 강남구 테헤란로 123")
                .phone("02-1234-5678")
                .email("grand@wedding.com")
                .build();

        testWeddingHall2 = WeddingHall.builder()
                .id(2L)
                .name("로얄 호텔")
                .venueType(WeddingHallEnum.HOTEL)
                .parking(200)
                .address("서울시 서초구 서초대로 456")
                .phone("02-8765-4321")
                .email("royal@hotel.com")
                .build();
    }

    @Test
    @DisplayName("웨딩홀 리스트 조회 - Repository가 빈 리스트를 반환하는 경우")
    void getWeddingInfos_WhenRepositoryReturnsEmptyList() {
        // given
        given(weddingHallRepository.findAll()).willReturn(Collections.emptyList());

        // when
        List<WeddingHallResponse> responses = weddingHallService.getWeddingInfos();

        // then
        assertThat(responses).isEmpty();
        verify(weddingHallRepository).findAll();
    }

    @Test
    @DisplayName("웨딩홀 리스트 조회 - Repository가 데이터를 반환하는 경우")
    void getWeddingInfos_WhenRepositoryReturnsData() {
        // given
        List<WeddingHall> weddingHalls = Arrays.asList(testWeddingHall1, testWeddingHall2);
        given(weddingHallRepository.findAll()).willReturn(weddingHalls);

        // when
        List<WeddingHallResponse> responses = weddingHallService.getWeddingInfos();

        // then
        assertThat(responses).hasSize(2);

        WeddingHallResponse response1 = responses.get(0);
        assertThat(response1.getId()).isEqualTo(1L);
        assertThat(response1.getName()).isEqualTo("그랜드 웨딩홀");
        assertThat(response1.getVenueType()).isEqualTo(WeddingHallEnum.WEDDING_HALL);

        WeddingHallResponse response2 = responses.get(1);
        assertThat(response2.getId()).isEqualTo(2L);
        assertThat(response2.getName()).isEqualTo("로얄 호텔");
        assertThat(response2.getVenueType()).isEqualTo(WeddingHallEnum.HOTEL);

        verify(weddingHallRepository).findAll();
    }

    /*
    이 부분은 exception class 생성 후 테스트 예정
    @Test
    @DisplayName("웨딩홀 상세정보 조회 - Repository가 빈 리스트를 반환하는 경우")
    void getWeddingInfo_WhenRepositoryReturnsEmptyList() {
        // given
        given(weddingHallRepository.getReferenceById(anyLong())).willReturn(null);

        // when
        WeddingHallResponse responses = weddingHallService.getWeddingInfo(anyLong());

        // then
        assertThat(responses).isNull();
        verify(weddingHallRepository).findById(anyLong());
    }*/

    @Test
    @DisplayName("웨딩홀 상세정보 조회 - Repository가 데이터를 반환하는 경우")
    void getWeddingInfo_WhenRepositoryReturnsData() {
        // given
        WeddingHall weddingHall = testWeddingHall1;
        given(weddingHallRepository.getReferenceById(testWeddingHall1.getId())).willReturn(weddingHall);

        // when
        WeddingHallResponse response = weddingHallService.getWeddingInfo(testWeddingHall1.getId());

        // then
        assertThat(response).isNotNull();

        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getName()).isEqualTo("그랜드 웨딩홀");
        assertThat(response.getVenueType()).isEqualTo(WeddingHallEnum.WEDDING_HALL);

        verify(weddingHallRepository).getReferenceById(testWeddingHall1.getId());
    }

    @Test
    @DisplayName("웨딩홀 저장 - 정상적인 요청")
    void saveWedding_WithValidRequest() {
        // given
        WeddingHallRequest request = new WeddingHallRequest();
        request.setName("신규 웨딩홀");
        request.setVenueType(WeddingHallEnum.OUTDOOR);
        request.setParking(300);
        request.setAddress("서울시 종로구 종로 789");
        request.setPhone("02-9999-8888");
        request.setEmail("new@wedding.com");

        WeddingHall expectedSavedWeddingHall = WeddingHall.builder()
                .id(3L)
                .name("신규 웨딩홀")
                .venueType(WeddingHallEnum.OUTDOOR)
                .parking(300)
                .address("서울시 종로구 종로 789")
                .phone("02-9999-8888")
                .email("new@wedding.com")
                .build();

        given(weddingHallRepository.save(any(WeddingHall.class))).willReturn(expectedSavedWeddingHall);

        // when
        boolean result = weddingHallService.saveWedding(request);

        // then
        assertThat(result).isTrue();
        verify(weddingHallRepository).save(any(WeddingHall.class));

        ArgumentCaptor<WeddingHall> weddingHallCaptor = ArgumentCaptor.forClass(WeddingHall.class);
        verify(weddingHallRepository).save(weddingHallCaptor.capture());
        WeddingHall savedWeddingHall = weddingHallCaptor.getValue();

        assertThat(savedWeddingHall.getVenueType()).isEqualTo(WeddingHallEnum.OUTDOOR);
        assertThat(savedWeddingHall.getParking()).isEqualTo(300);
        assertThat(savedWeddingHall.getAddress()).isEqualTo("서울시 종로구 종로 789");
        assertThat(savedWeddingHall.getPhone()).isEqualTo("02-9999-8888");
        assertThat(savedWeddingHall.getEmail()).isEqualTo("new@wedding.com");

        verify(weddingHallRepository).save(any(WeddingHall.class));
    }

    @Test
    @DisplayName("웨딩홀 저장 - Request를 Entity로 변환하는 로직 검증")
    void saveWedding_RequestToEntityConversion() {
        // given
        WeddingHallRequest request = new WeddingHallRequest();
        request.setName("변환 테스트홀");
        request.setVenueType(WeddingHallEnum.WEDDING_HALL);
        request.setParking(150);
        request.setAddress("서울시 마포구");
        request.setPhone("02-1111-2222");
        request.setEmail("convert@test.com");

        WeddingHall mockSavedEntity = WeddingHall.builder()
                .id(4L)
                .name("변환 테스트홀")
                .venueType(WeddingHallEnum.WEDDING_HALL)
                .parking(150)
                .address("서울시 마포구")
                .phone("02-1111-2222")
                .email("convert@test.com")
                .build();

        given(weddingHallRepository.save(any(WeddingHall.class))).willReturn(mockSavedEntity);

        // when
        boolean result = weddingHallService.saveWedding(request);

        // then
        assertThat(result).isTrue();

        verify(weddingHallRepository).save(any(WeddingHall.class));
    }

    @Test
    @DisplayName("웨딩홀 삭제 성공합니다.")
    void deleteWedding_success() {
        // given
        WeddingHallRequest request = new WeddingHallRequest();
        request.setName("신규 웨딩홀");
        request.setVenueType(WeddingHallEnum.OUTDOOR);
        request.setParking(300);
        request.setAddress("서울시 종로구 종로 789");
        request.setPhone("02-9999-8888");
        request.setEmail("new@wedding.com");

        WeddingHall expectedSavedWeddingHall = WeddingHall.builder()
                .id(3L)
                .name("신규 웨딩홀")
                .venueType(WeddingHallEnum.OUTDOOR)
                .parking(300)
                .address("서울시 종로구 종로 789")
                .phone("02-9999-8888")
                .email("new@wedding.com")
                .build();

        given(weddingHallRepository.save(any(WeddingHall.class))).willReturn(expectedSavedWeddingHall);

        // when
        boolean result = weddingHallService.saveWedding(request);

        // then
        assertThat(result).isTrue();
        verify(weddingHallRepository).save(any(WeddingHall.class));

        ArgumentCaptor<WeddingHall> weddingHallCaptor = ArgumentCaptor.forClass(WeddingHall.class);
        verify(weddingHallRepository).save(weddingHallCaptor.capture());
        WeddingHall savedWeddingHall = weddingHallCaptor.getValue();

        assertThat(savedWeddingHall.getVenueType()).isEqualTo(WeddingHallEnum.OUTDOOR);
        assertThat(savedWeddingHall.getParking()).isEqualTo(300);
        assertThat(savedWeddingHall.getAddress()).isEqualTo("서울시 종로구 종로 789");
        assertThat(savedWeddingHall.getPhone()).isEqualTo("02-9999-8888");
        assertThat(savedWeddingHall.getEmail()).isEqualTo("new@wedding.com");

        verify(weddingHallRepository).save(any(WeddingHall.class));
    }

    @DisplayName("웨딩홀 데이터 삭제에 성공합니다.")
    @Test
    void deleteWeddingTest_success() {
        // given
        when(weddingHallRepository.existsById(anyLong())).thenReturn(true);
        doNothing().when(weddingHallRepository).deleteById(anyLong());

        // when
        boolean result = weddingHallService.deleteWedding(1L);

        // then
        assertThat(result).isTrue();
    }

    @DisplayName("id가 존재하지 않는 경우 데이터 삭제를 시도합니다.")
    @Test
    void deleteWeddingTest_Is_Not_Exist() {
        // given
        when(weddingHallRepository.existsById(1L)).thenReturn(false);

        // when
        boolean result = weddingHallService.deleteWedding(1L);

        // then
        assertThat(result).isFalse();

        verify(weddingHallRepository).existsById(1L);
        verify(weddingHallRepository, never()).deleteById(any());
    }

//    @Override
//    public boolean deleteWedding(Long id) {
//        if (!weddingHallRepository.existsById(id))
//            return false;
//
//        try{
//            weddingHallRepository.deleteById(id);
//            return true;
//        } catch (DataIntegrityViolationException e) {
//            throw e;
//        }
//    }
}