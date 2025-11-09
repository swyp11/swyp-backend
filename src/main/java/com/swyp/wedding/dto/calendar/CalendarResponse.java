package com.swyp.wedding.dto.calendar;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import com.swyp.wedding.entity.calendar.Calendar;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CalendarResponse {
    private Long id;
    private String title;
    private String memo;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static CalendarResponse from(Calendar entity) {
        return CalendarResponse.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .memo(entity.getMemo())
                .startTime(entity.getStartTime())
                .endTime(entity.getEndTime())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
