package com.swyp.wedding.controller.weddinghall;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.swyp.wedding.dto.weddinghall.WeddingHallRequest;
import com.swyp.wedding.dto.weddinghall.WeddingHallResponse;
import com.swyp.wedding.entity.common.SortType;
import com.swyp.wedding.entity.weddinghall.WeddingHall;
import com.swyp.wedding.entity.weddinghall.WeddingHallEnum;
import com.swyp.wedding.service.weddinghall.impl.WeddingHallServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(value = WeddingApiController.class, 
    excludeAutoConfiguration = {
        org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class,
        org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration.class
    })
@DisplayName("WeddingApiController 테스트")
@MockBean(JpaMetamodelMappingContext.class)
class WeddingApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WeddingHallServiceImpl weddingHallService;

    @Autowired
    private ObjectMapper objectMapper;

    private WeddingHallResponse testResponse1;
    private WeddingHallResponse testResponse2;
    private WeddingHallRequest testRequest;
    private WeddingHall testWeddingHall;

    @BeforeEach
    void setUp() {
        testResponse1 = new WeddingHallResponse();
        testResponse1.setId(1L);
        testResponse1.setName("그랜드 웨딩홀");
        testResponse1.setVenueType(WeddingHallEnum.WEDDING_HALL);
        testResponse1.setParking(100);
        testResponse1.setAddress("서울시 강남구 테헤란로 123");
        testResponse1.setPhone("02-1234-5678");
        testResponse1.setEmail("grand@wedding.com");

        testResponse2 = new WeddingHallResponse();
        testResponse2.setId(2L);
        testResponse2.setName("로얄 호텔");
        testResponse2.setVenueType(WeddingHallEnum.HOTEL);
        testResponse2.setParking(200);
        testResponse2.setAddress("서울시 서초구 서초대로 456");
        testResponse2.setPhone("02-8765-4321");
        testResponse2.setEmail("royal@hotel.com");

        testRequest = new WeddingHallRequest();
        testRequest.setName("신규 웨딩홀");
        testRequest.setVenueType(WeddingHallEnum.OUTDOOR);
        testRequest.setParking(150);
        testRequest.setAddress("서울시 종로구 종로 789");
        testRequest.setPhone("02-9999-8888");
        testRequest.setEmail("new@wedding.com");

        testWeddingHall = WeddingHall.builder()
                .id(3L)
                .name("신규 웨딩홀")
                .venueType(WeddingHallEnum.OUTDOOR)
                .parking(150)
                .address("서울시 종로구 종로 789")
                .phone("02-9999-8888")
                .email("new@wedding.com")
                .build();
    }

    @Test
    @DisplayName("GET /api/wedding - 웨딩홀 리스트 조회 성공 (기본값)")
    void getWeddings_Success() throws Exception {
        // given
        List<WeddingHallResponse> responses = Arrays.asList(testResponse1, testResponse2);
        given(weddingHallService.getWeddingInfos(null)).willReturn(responses);

        // when & then
        mockMvc.perform(get("/api/wedding"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("그랜드 웨딩홀"))
                .andExpect(jsonPath("$[0].venueType").value("WEDDING_HALL"))
                .andExpect(jsonPath("$[0].parking").value(100))
                .andExpect(jsonPath("$[0].address").value("서울시 강남구 테헤란로 123"))
                .andExpect(jsonPath("$[0].phone").value("02-1234-5678"))
                .andExpect(jsonPath("$[0].email").value("grand@wedding.com"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("로얄 호텔"))
                .andExpect(jsonPath("$[1].venueType").value("HOTEL"));
    }

    @Test
    @DisplayName("GET /api/wedding - 빈 리스트 조회")
    void getWeddings_EmptyList() throws Exception {
        // given
        given(weddingHallService.getWeddingInfos(null)).willReturn(Arrays.asList());

        // when & then
        mockMvc.perform(get("/api/wedding"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    @DisplayName("GET /api/wedding?sort=RECENT - 웨딩홀 리스트 최신순 조회")
    void getWeddings_RecentSort() throws Exception {
        // given
        List<WeddingHallResponse> responses = Arrays.asList(testResponse1, testResponse2);
        given(weddingHallService.getWeddingInfos(SortType.RECENT)).willReturn(responses);

        // when & then
        mockMvc.perform(get("/api/wedding")
                        .param("sort", "RECENT"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("그랜드 웨딩홀"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("로얄 호텔"));
    }

    @Test
    @DisplayName("GET /api/wedding?sort=FAVORITE - 웨딩홀 리스트 인기순 조회")
    void getWeddings_FavoriteSort() throws Exception {
        // given
        List<WeddingHallResponse> responses = Arrays.asList(testResponse2, testResponse1);
        given(weddingHallService.getWeddingInfos(SortType.FAVORITE)).willReturn(responses);

        // when & then
        mockMvc.perform(get("/api/wedding")
                        .param("sort", "FAVORITE"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(2))
                .andExpect(jsonPath("$[0].name").value("로얄 호텔"))
                .andExpect(jsonPath("$[1].id").value(1))
                .andExpect(jsonPath("$[1].name").value("그랜드 웨딩홀"));
    }

    @Test
    @DisplayName("GET /api/wedding - 웨딩홀 상세정보 조회")
    void getWeddingInfo_Success() throws Exception {
        // given
        WeddingHallResponse responses = testResponse1;
        given(weddingHallService.getWeddingInfo(anyLong())).willReturn(responses);

        // when & then
        mockMvc.perform(get("/api/wedding/{id}", 1L))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("그랜드 웨딩홀"))
                .andExpect(jsonPath("$.venueType").value("WEDDING_HALL"))
                .andExpect(jsonPath("$.parking").value(100))
                .andExpect(jsonPath("$.address").value("서울시 강남구 테헤란로 123"))
                .andExpect(jsonPath("$.phone").value("02-1234-5678"))
                .andExpect(jsonPath("$.email").value("grand@wedding.com"));
    }

    @Test
    @DisplayName("GET /api/wedding - 빈 리스트 조회")
    void getWeddingInfo_EmptyList() throws Exception {
        // given
        given(weddingHallService.getWeddingInfo(anyLong())).willReturn(new WeddingHallResponse());

        // when & then
        mockMvc.perform(get("/api/wedding"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isEmpty())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    @DisplayName("POST /api/wedding - 웨딩홀 저장 성공")
    void saveWedding_Success() throws Exception {
        // given
        given(weddingHallService.saveWedding(any(WeddingHallRequest.class)))
                .willReturn(true);

        // when & then
        mockMvc.perform(post("/api/wedding")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.result").value(true));
    }

    @Test
    @DisplayName("POST /api/wedding - 웨딩홀 저장 실패 (false 반환)")
    void saveWedding_Failure() throws Exception {
        // given
        given(weddingHallService.saveWedding(any(WeddingHallRequest.class)))
                .willReturn(false);

        // when & then
        mockMvc.perform(post("/api/wedding")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.result").value(false));
    }

    @Test
    @DisplayName("웨딩홀 정보를 삭제에 성공합니다.")
    void deleteWedding_Success() throws Exception {
        // given
        given(weddingHallService.deleteWedding(anyLong()))
                .willReturn(true);

        // when & then
        mockMvc.perform(delete("/api/wedding/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.result").value(true));
    }

    @Test
    @DisplayName("웨딩홀 정보 삭제에 실패합니다.")
    void deleteWedding_Failure() throws Exception {
        // given
        given(weddingHallService.deleteWedding(anyLong()))
                .willReturn(false);

        // when & then
        mockMvc.perform(delete("/api/wedding/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.result").value(false));
    }
}