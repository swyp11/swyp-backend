package com.swyp.wedding.controller.schedule;

import com.swyp.wedding.dto.schedule.ScheduleRequest;
import com.swyp.wedding.dto.schedule.ScheduleResponse;
import com.swyp.wedding.dto.schedule.ScheduleMonthResponse;
import com.swyp.wedding.dto.schedule.ScheduleWeekResponse;
import com.swyp.wedding.global.response.ApiResponse;
import com.swyp.wedding.security.user.CustomUserDetails;
import com.swyp.wedding.service.ScheduleService;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Tag(name = "일정", description = "결혼 준비 일정 관리 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/schedule")
public class ScheduleController {

    private final ScheduleService scheduleService;

    @PostMapping
    public ResponseEntity<ApiResponse<ScheduleResponse>> createEvent(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody ScheduleRequest scheduleRequest){

        ScheduleResponse response =  scheduleService.createEvent(userDetails.getUsername(), scheduleRequest);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Schema(description = "월별 조회")
    @GetMapping("/month")
    public ResponseEntity<ApiResponse<List<ScheduleMonthResponse>>> getMonthEvents(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam int year, int month) {
        return ResponseEntity.ok(ApiResponse.success(scheduleService.getMonthEvents(userDetails.getUsername(), year, month)));
    }

    @Schema(description = "주별 조회")
    @GetMapping("/week")
    public ResponseEntity<ApiResponse<List<ScheduleWeekResponse>>> getWeekEvents(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam LocalDate startDate) {
        List<ScheduleWeekResponse> responses = scheduleService.getWeekEvents(userDetails.getUsername(), startDate);
        return ResponseEntity.ok(ApiResponse.success(responses));
    }

    @Schema(description = "하루(당일) 조회")
    @GetMapping("/day")
    public ResponseEntity<ApiResponse<List<ScheduleResponse>>> getDayEvents(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam LocalDate date) {
        List<ScheduleResponse> response = scheduleService.getDayEvents(userDetails.getUsername(),date);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Schema(description = "스케쥴 하나의 데이터 조회")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ScheduleResponse>> getEventId(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long id) {
        ScheduleResponse response = scheduleService.getEventById(userDetails.getUsername(),id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Schema(description = "수정")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ScheduleResponse>> updateEvent(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long id,
            @RequestBody ScheduleRequest request) {
        ScheduleResponse response = scheduleService.updateEvent(userDetails.getUsername(),id, request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Schema(description = "삭제")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteEvent(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long id) {
        scheduleService.deleteEvent(userDetails.getUsername(),id);
        return ResponseEntity.ok(ApiResponse.success());
    }
}
