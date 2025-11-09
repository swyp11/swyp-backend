package com.swyp.wedding.controller.hall;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.swyp.wedding.dto.hall.HallRequest;
import com.swyp.wedding.dto.hall.HallResponse;
import com.swyp.wedding.dto.weddinghall.WeddingHallRequest;
import com.swyp.wedding.dto.weddinghall.WeddingHallResponse;
import com.swyp.wedding.entity.hall.HallType;
import com.swyp.wedding.entity.hall.LightType;
import com.swyp.wedding.entity.weddinghall.WeddingHall;
import com.swyp.wedding.entity.weddinghall.WeddingHallEnum;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.swyp.wedding.entity.hall.Hall;
import com.swyp.wedding.service.hall.HallService;
import com.swyp.wedding.service.hall.impl.HallServiceImpl;

@WebMvcTest(HallApiController.class)
@DisplayName("HallApiController 테스트")
public class HallApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private HallServiceImpl hallService;

        private HallRequest request;

        @Autowired
        private ObjectMapper objectMapper;

        private HallResponse testResponse1;
        private HallResponse testResponse2;
        private HallRequest testRequest;
        private Hall testHall;

        @BeforeEach
        void setup() {
                // 준비할 요청 객체 (컨트롤러에 보낼 JSON)
                testRequest = new HallRequest();
                testRequest.setName("루미에르 홀");
                testRequest.setCapacityMin(80);
                testRequest.setCapacityMax(220);
                testRequest.setHallType(HallType.SINGLE);
                testRequest.setFloorNo(2);
                testRequest.setLightType(LightType.BRIGHT);
                testRequest.setAreaM2(new BigDecimal("320.50"));
                testRequest.setCeilingHeight(new BigDecimal("6.20"));
                testRequest.setStage(true);
                testRequest.setLedWall(false);
                testRequest.setAisleLength(new BigDecimal("18.0"));
                testRequest.setPillar(true);
                testRequest.setStatus(true);
                testRequest.setDescription("자연광 + 샹들리에");

                // 응답 샘플 1
                testResponse1 = new HallResponse();
                testResponse1.setId(1L);
                testResponse1.setName("루미에르 홀");
                testResponse1.setCapacityMin(80);
                testResponse1.setCapacityMax(220);
                testResponse1.setHallType(HallType.SINGLE);
                testResponse1.setFloorNo(2);
                testResponse1.setLightType(LightType.BRIGHT);
                testResponse1.setAreaM2(new BigDecimal("320.50"));
                testResponse1.setCeilingHeight(new BigDecimal("6.20"));
                testResponse1.setStage(true);
                testResponse1.setLedWall(false);
                testResponse1.setAisleLength(new BigDecimal("18.0"));
                testResponse1.setPillar(true);
                testResponse1.setStatus(true);
                testResponse1.setDesc("자연광 + 샹들리에");

                // 응답 샘플 2 (다른 값)
                testResponse2 = new HallResponse();
                testResponse2.setId(2L);
                testResponse2.setName("벨라루체 홀");
                testResponse2.setCapacityMin(120);
                testResponse2.setCapacityMax(350);
                testResponse2.setHallType(HallType.COMPLEX);
                testResponse2.setFloorNo(5);
                testResponse2.setLightType(LightType.BRIGHT);
                testResponse2.setAreaM2(new BigDecimal("520.00"));
                testResponse2.setCeilingHeight(new BigDecimal("7.50"));
                testResponse2.setStage(true);
                testResponse2.setLedWall(true);
                testResponse2.setAisleLength(new BigDecimal("22.5"));
                testResponse2.setPillar(false);
                testResponse2.setStatus(true);
                testResponse2.setDesc("모던 인테리어 + LED 월");

                testHall = testRequest.toEntity();
        }

        @Test
        @DisplayName("GET /api/hall - 웨딩홀 리스트 조회 성공")
        void getWeddings_Success() throws Exception {
                // given
                List<HallResponse> responses = Arrays.asList(testResponse1, testResponse2);
                given(hallService.getHallInfos()).willReturn(responses);

                // when & then
                mockMvc.perform(get("/api/hall"))
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
        @DisplayName("GET /api/hall - 빈 리스트 조회")
        void getWeddings_EmptyList() throws Exception {
                // given
                given(hallService.getHallInfos()).willReturn(Arrays.asList());

                // when & then
                mockMvc.perform(get("/api/hall"))
                                .andDo(print())
                                .andExpect(status().isOk())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                .andExpect(jsonPath("$").isArray())
                                .andExpect(jsonPath("$.length()").value(0));
        }

        @Test
        @DisplayName("GET /api/hall - 웨딩홀 상세정보 조회")
        void getWeddingInfo_Success() throws Exception {
                // given
                HallResponse responses = testResponse1;
                given(hallService.getHallInfo(anyLong())).willReturn(responses);

                // when & then
                mockMvc.perform(get("/api/hall/{id}", 1L))
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
        @DisplayName("GET /api/hall - 빈 리스트 조회")
        void getWeddingInfo_EmptyList() throws Exception {
                // given
                given(hallService.getHallInfo(anyLong())).willReturn(new HallResponse());

                // when & then
                mockMvc.perform(get("/api/hall"))
                                .andDo(print())
                                .andExpect(status().isOk())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                .andExpect(jsonPath("$").isEmpty())
                                .andExpect(jsonPath("$.length()").value(0));
        }

        @Test
        @DisplayName("POST /api/hall - 웨딩홀 저장 성공")
        void saveWedding_Success() throws Exception {
                // given
                given(hallService.saveHall(any(HallRequest.class)))
                                .willReturn(true);

                // when & then
                mockMvc.perform(post("/api/hall")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(testRequest)))
                                .andDo(print())
                                .andExpect(status().isOk())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                .andExpect(jsonPath("$.result").value(true));
        }

        @Test
        @DisplayName("POST /api/hall - 웨딩홀 저장 실패 (false 반환)")
        void saveWedding_Failure() throws Exception {
                // given
                given(hallService.saveHall(any(HallRequest.class)))
                                .willReturn(false);

                // when & then
                mockMvc.perform(post("/api/hall")
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
                given(hallService.deleteHall(anyLong()))
                                .willReturn(true);

                // when & then
                mockMvc.perform(delete("/api/hall/{id}", 1L)
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
                given(hallService.deleteHall(anyLong()))
                                .willReturn(false);

                // when & then
                mockMvc.perform(delete("/api/hall/{id}", 1L)
                                .contentType(MediaType.APPLICATION_JSON))
                                .andDo(print())
                                .andExpect(status().isOk())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                .andExpect(jsonPath("$.result").value(false));
        }
}
