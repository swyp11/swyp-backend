package com.swyp.wedding.dto.schedule;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import com.swyp.wedding.entity.schedule.*;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
@Builder
public class ScheduleMonthResponse {
    private long id;
    private String title;
    private LocalDate startDate;
    private LocalDate endDate;

    public static ScheduleMonthResponse fromEntity(Schedule schedule) {
        return ScheduleMonthResponse.builder()
                .id(schedule.getId())
                .startDate(schedule.getStartDate())
                .endDate(schedule.getEndDate())
                .title(schedule.getTitle())
                .build();
    }
}
