package com.swyp.wedding.dto.calendar;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class CalendarRequest {
    private String title;
    private String memo;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

}
