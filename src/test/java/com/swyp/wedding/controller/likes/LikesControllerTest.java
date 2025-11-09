 package com.swyp.wedding.controller.likes;


 import com.fasterxml.jackson.databind.ObjectMapper;
 import com.swyp.wedding.controller.hall.HallApiController;
 import com.swyp.wedding.dto.hall.HallResponse;
 import com.swyp.wedding.dto.user.UserRequest;
 import com.swyp.wedding.entity.user.User;
 import com.swyp.wedding.entity.user.UserEnum;
 import com.swyp.wedding.security.user.CustomUserDetails;
 import com.swyp.wedding.service.likes.impl.LikesServiceImpl;
 import org.junit.jupiter.api.BeforeEach;
 import org.junit.jupiter.api.DisplayName;
 import org.junit.jupiter.api.Test;
 import org.mockito.Mock;
 import org.springframework.beans.factory.annotation.Autowired;
 import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
 import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
 import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
 import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
 import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
 import org.springframework.boot.test.mock.mockito.MockBean;
 import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
 import org.springframework.http.MediaType;
 import org.springframework.test.context.ActiveProfiles;
 import org.springframework.test.web.servlet.MockMvc;

 import java.time.LocalDate;
 import java.util.Arrays;
 import java.util.List;

 import static org.junit.jupiter.api.Assertions.*;
 import static org.mockito.ArgumentMatchers.anyLong;
 import static org.mockito.ArgumentMatchers.anyString;
 import static org.mockito.BDDMockito.given;
 import static org.mockito.Mockito.when;
 import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
 import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
 import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
@WebMvcTest(controllers = LikesController.class,
        excludeAutoConfiguration = {
                HibernateJpaAutoConfiguration.class,
                JpaRepositoriesAutoConfiguration.class,
                org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class,
                org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration.class
        })
@ActiveProfiles("test")
@DisplayName("LikesController 테스트")
@MockBean(JpaMetamodelMappingContext.class)
 class LikesControllerTest {

     @Autowired
     private MockMvc mockMvc;

     @MockBean
     private LikesServiceImpl likesService;

     @Autowired
     private ObjectMapper objectMapper;

     private User user;

     @BeforeEach
     void setup() {
         UserRequest userRequest = new UserRequest();
         userRequest.setId(1L);
         userRequest.setName("test");
         userRequest.setAuth(UserEnum.USER);
         userRequest.setEmail("test@gmail.com");
         userRequest.setBirth(LocalDate.of(2000, 1, 1));
         userRequest.setPassword("test");
         userRequest.setAddress("서울시");
         userRequest.setPhoneNumber("010-1111-1111");
         userRequest.setProvider("google");

         user = userRequest.toEntity();
     }

     @DisplayName("POST /api/likes/{category}/{postId} - 찜하기 성공")
     @Test
     void storeLikes_Success() throws Exception {
         // given
         when(likesService.storeLikes(anyString(), anyLong(), anyString())).thenReturn(true);

         // when & then
         mockMvc.perform(post("/api/likes/{category}/{postId}", "Hall", 1L)
                 .principal(() -> "testUser"))
                 .andDo(print())
                 .andExpect(status().isOk())
                 .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                 .andExpect(jsonPath("$.result").value(true));
     }

    @DisplayName("POST /api/likes/{category}/{postId} - 찜하기 실패 (없는 postId 일 경우)")
    @Test
    void storeLikes_Fail() throws Exception {
        // given
        when(likesService.storeLikes(anyString(), anyLong(), anyString())).thenReturn(false);

        // when & then
        mockMvc.perform(post("/api/likes/{category}/{postId}", "Hall", 9999L)
                        .principal(() -> "testUser"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.result").value(false));
    }
 }