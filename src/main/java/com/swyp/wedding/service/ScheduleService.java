package com.swyp.wedding.service;

import com.swyp.wedding.dto.schedule.ScheduleRequest;
import com.swyp.wedding.dto.schedule.ScheduleResponse;
import com.swyp.wedding.dto.schedule.ScheduleMonthResponse;
import com.swyp.wedding.dto.schedule.ScheduleWeekResponse;
import com.swyp.wedding.entity.schedule.Schedule;
import com.swyp.wedding.entity.user.User;
import com.swyp.wedding.repository.schedule.ScheduleRepository;
import com.swyp.wedding.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final UserRepository userRepository;
    private final ScheduleRepository scheduleRepository;

    // 이벤트 생성
    @Transactional
    public ScheduleResponse createEvent(String username, ScheduleRequest scheduleRequest) {

        // 사용자 조회 (userId로 User 엔티티 찾기)
        User user = userRepository.findByUserId(username)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        Schedule schedule = Schedule.builder()
                .title(scheduleRequest.getTitle())
                .memo(scheduleRequest.getMemo())
                .startDate(scheduleRequest.getStartDate())
                .endDate(scheduleRequest.getEndDate())
                .startTime(scheduleRequest.getStartTime())
                .endTime(scheduleRequest.getEndTime())
                .user(user)
                .build();

        Schedule saved = scheduleRepository.save(schedule);

        return ScheduleResponse.fromEntity(saved);
    }

    // 월별 조회
    public List<ScheduleMonthResponse> getMonthEvents(String username, int year, int month) {
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());
        return scheduleRepository.findByUser_UserIdAndStartDateBetween(username, startDate, endDate)
                .stream()
                .map(ScheduleMonthResponse::fromEntity)
                .toList();
    }

    // 주별 조회
    public List<ScheduleWeekResponse> getWeekEvents(String username, LocalDate startDate) {
        // 7일 범위 자동 계산
        LocalDate endDate = startDate.plusDays(6);
        return scheduleRepository.findByUser_UserIdAndStartDateBetween(username, startDate, endDate)
                .stream()
                .map(ScheduleWeekResponse::fromEntity)
                .toList();
    }

    // 일별 조회
    public List<ScheduleResponse> getDayEvents(String username, LocalDate date) {
        return scheduleRepository.findByUser_UserIdAndStartDate(username,date)
                .stream()
                .map(ScheduleResponse::fromEntity)
                .toList();
    }

    // 스케줄 하나 조회
    @Transactional(readOnly = true)
    public ScheduleResponse getEventById(String username, Long id) {
        Schedule schedule =  scheduleRepository.findByIdAndUser_UserId(id, username)
                .orElseThrow(()-> new IllegalArgumentException("해당일정이 없거나 접근 권한이 없습니다."));
        return ScheduleResponse.fromEntity(schedule);
    }


    // 수정
    @Transactional
    public ScheduleResponse updateEvent(String username, Long id, ScheduleRequest request) {
        Schedule schedule= scheduleRepository.findByIdAndUser_UserId(id,username)
                .orElseThrow(() -> new IllegalArgumentException("해당 일정이 존재하지 않습니다. id=" + id));

        // 엔티티 내부 update 메서드 이용
        schedule.update(request);

        // save 필요 없음 ->JPA의 dirty checking으로 자동 update됨
        return ScheduleResponse.fromEntity(schedule);
    }

    @Transactional
    public void deleteEvent(String username , Long id) {
        Schedule schedule = scheduleRepository.findByIdAndUser_UserId(id, username)
                .orElseThrow(() -> new IllegalArgumentException("해당 일정이 없거나 권한이 없습니다. id=" + id));

        scheduleRepository.delete(schedule);
    }

}

