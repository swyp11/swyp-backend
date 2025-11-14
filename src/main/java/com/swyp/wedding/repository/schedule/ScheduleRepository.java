package com.swyp.wedding.repository.schedule;

import com.swyp.wedding.entity.schedule.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

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

    // id별 조회


}
