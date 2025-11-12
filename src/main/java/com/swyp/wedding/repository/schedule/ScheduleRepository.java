package com.swyp.wedding.repository.schedule;

import com.swyp.wedding.entity.schedule.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ScheduleRepository extends JpaRepository <Schedule, Long>{
    // 특정 기간 내의 모든 일정 조회
    List<Schedule> findByUser_UserIdAndStartDateBetween(String userId, LocalDate start, LocalDate end);

    // 하루 일정 조회
    List<Schedule> findByUser_UserIdAndStartDate(String userId, LocalDate date);

    Optional<Schedule> findByIdAndUser_UserId(Long id, String username);

    // id별 조회


}
