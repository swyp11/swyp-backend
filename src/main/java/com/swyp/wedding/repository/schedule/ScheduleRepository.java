package com.swyp.wedding.repository.schedule;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.swyp.wedding.entity.schedule.Schedule;

@Repository
public interface ScheduleRepository extends JpaRepository <Schedule, Long>{
    // 특정 기간 내의 모든 일정 조회 (기간과 겹치는 모든 일정)
    // 조건: schedule.startDate <= searchEnd AND schedule.endDate >= searchStart
    List<Schedule> findByUser_UserIdAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
            String userId, LocalDate searchEnd, LocalDate searchStart);

    // 하루 일정 조회 (사용 안 함 - 호환성 유지용)
    @Deprecated
    List<Schedule> findByUser_UserIdAndStartDate(String userId, LocalDate date);

    Optional<Schedule> findByIdAndUser_UserId(Long id, String username);

    // 알림 보낼 일정 조회 (모든 사용자)
    List<Schedule> findByUser_UserId(String userId);

}
