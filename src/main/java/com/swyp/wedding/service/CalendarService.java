package com.swyp.wedding.service;

import com.swyp.wedding.dto.calendar.CalendarRequest;
import com.swyp.wedding.dto.calendar.CalendarResponse;
import com.swyp.wedding.entity.calendar.Calendar;
import com.swyp.wedding.repository.calendar.CalendarRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CalendarService {

    private final CalendarRepository calendarRepository;

    // 이벤트 생성
    public CalendarResponse createEvent(CalendarRequest calendarRequest) {

        Calendar calendar = Calendar.builder()
                .title(calendarRequest.getTitle())
                .memo(calendarRequest.getMemo())
                .startTime(calendarRequest.getStartTime())
                .endTime(calendarRequest.getEndTime())
                .build();

        Calendar saved = calendarRepository.save(calendar);

        return CalendarResponse.from(saved);
    }

    // 전체 조회
    public List<CalendarResponse> getAllEvents() {
        return calendarRepository.findAll().stream()
                .map(CalendarResponse::from)
                .toList();
    }

    // 단일 조회
    public CalendarResponse getEventById(Long id) {
        var calendar = calendarRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 일정이 존재하지 않습니다. id=" + id));

        return CalendarResponse.from(calendar);
    }

    // 수정
    public CalendarResponse updateEvent(Long id, CalendarRequest request) {
        var calendar = calendarRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 일정이 존재하지 않습니다. id=" + id));

        // 엔티티 수정
        Calendar updated = Calendar.builder()
                .id(calendar.getId())
                .title(request.getTitle())
                .memo(request.getMemo())
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .createdAt(calendar.getCreatedAt())
                .build();

        Calendar saved = calendarRepository.save(updated);
        return CalendarResponse.from(saved);
    }

    public void deleteEvent(Long id) {
        if (!calendarRepository.existsById(id)) {
            throw new IllegalArgumentException("해당 일정이 존재하지 않습니다. id=" + id);
        }
        calendarRepository.deleteById(id);
    }
}

