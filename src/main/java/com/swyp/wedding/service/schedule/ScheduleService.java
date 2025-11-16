package com.swyp.wedding.service.schedule;

import com.swyp.wedding.dto.schedule.ScheduleRequest;
import com.swyp.wedding.dto.schedule.ScheduleResponse;
import com.swyp.wedding.dto.schedule.ScheduleMonthResponse;
import com.swyp.wedding.dto.schedule.ScheduleWeekResponse;
import com.swyp.wedding.entity.schedule.Schedule;
import com.swyp.wedding.entity.user.User;
import com.swyp.wedding.repository.schedule.ScheduleRepository;
import com.swyp.wedding.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class  ScheduleService {

    private final UserRepository userRepository;
    private final ScheduleRepository scheduleRepository;
    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();

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

    // 월별 조회 (해당 월과 겹치는 모든 일정 조회)
    @Transactional(readOnly = true)
    public List<ScheduleMonthResponse> getMonthEvents(String username, int year, int month) {
        LocalDate searchStart = LocalDate.of(year, month, 1);
        LocalDate searchEnd = searchStart.withDayOfMonth(searchStart.lengthOfMonth());
        return scheduleRepository.findByUser_UserIdAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
                        username, searchEnd, searchStart)
                .stream()
                .map(ScheduleMonthResponse::fromEntity)
                .toList();
    }

    // 주별 조회 (해당 주와 겹치는 모든 일정 조회)
    @Transactional(readOnly = true)
    public List<ScheduleWeekResponse> getWeekEvents(String username, LocalDate startDate) {
        // 7일 범위 자동 계산
        LocalDate searchStart = startDate;
        LocalDate searchEnd = startDate.plusDays(6);
        return scheduleRepository.findByUser_UserIdAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
                        username, searchEnd, searchStart)
                .stream()
                .map(ScheduleWeekResponse::fromEntity)
                .toList();
    }

    // 일별 조회 (해당 날짜를 포함하는 모든 일정 조회)
    @Transactional(readOnly = true)
    public List<ScheduleResponse> getDayEvents(String username, LocalDate date) {
        return scheduleRepository.findByUser_UserIdAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
                        username, date, date)
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

//    @Scheduled(cron = "0 * * * * *")
//    @Transactional
//    public void sendAlarms(String userId) {
//        LocalDateTime now = LocalDateTime.now();
//
//        // 아직 알림 안 보낸 일정들
//        List<Schedule> schedules = scheduleRepository.findByUser_UserIdAndNotifiedAtIsNull(userId);
//
//        schedules.stream()
//                .filter(s -> isAlarmTime(now, s))
//                .forEach(s -> {
//                    // 1) 여기서 실제 알림 처리(추후 이메일/푸시 연동)
//                    log.info("[SCHEDULE ALARM] userId={}, title='{}', start={}",
//                            s.getUser().getId(), s.getTitle(), s.getEndTime());
//
//                    // 2) 알림 보냈다고 표시
//                    s.markNotified();
//                });
//    }
//
//    private boolean isAlarmTime(LocalDateTime now, Schedule schedule) {
//        LocalDateTime start = schedule.getStartDate().atTime(schedule.getStartTime());
//        LocalDateTime notifyAt = start.minusMinutes(
//                schedule.getNotifyBeforeMinutes() == null ? 10 : schedule.getNotifyBeforeMinutes()
//        );
//
//        // 예: now 가 notifyAt ~ start 사이에 들어오면 알림
//        return !now.isBefore(notifyAt) && !now.isAfter(start);
//    }
//
//
//    public SseEmitter subscribe(String userId) {
//        SseEmitter emitter = new SseEmitter(0L);
//
//        emitters.put(userId, emitter);
//
//        emitter.onCompletion(() -> emitters.remove(userId));
//        emitter.onTimeout(() -> emitters.remove(userId));
//        emitter.onError(e -> emitters.remove(userId));
//
//        try {
//            emitter.send(SseEmitter.event()
//                    .name("connect")
//                    .data("connected"));
//        } catch (IOException e) {
//            emitters.remove(userId);
//        }
//
//        return emitter;
//    }

    public List<ScheduleResponse> getNotifications(String username) {
        List<Schedule> schedules = scheduleRepository.findByUser_UserId();
        List<ScheduleResponse> result = new ArrayList<>();

        for (Schedule tmp : schedules) {
            LocalDateTime st = tmp.getEndDate().atTime(tmp.getEndTime());
            LocalDateTime now = LocalDateTime.now();

            boolean isWithinOneDay =
                    !st.isBefore(now) &&  // st >= now
                            st.isBefore(now.plusDays(1)); // st < now + 1일

            if (isWithinOneDay) {
                result.add(ScheduleResponse.fromEntity(tmp));
            }

        }

        return result;
    }
}

