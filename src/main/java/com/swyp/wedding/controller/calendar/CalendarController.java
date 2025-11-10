package com.swyp.wedding.controller.calendar;

import com.swyp.wedding.dto.calendar.CalendarRequest;
import com.swyp.wedding.dto.calendar.CalendarResponse;
import com.swyp.wedding.service.CalendarService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor

public class CalendarController {

    private final CalendarService calendarService;

    @PostMapping("/calendar")
    public ResponseEntity<CalendarResponse> createEvent(@RequestBody CalendarRequest calendarRequest){

        CalendarResponse response =  calendarService.createEvent(calendarRequest);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/calendar")
    public ResponseEntity<List<CalendarResponse>> getAllEvents() {
        List<CalendarResponse> responses = calendarService.getAllEvents();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/calendar/{id}")
    public ResponseEntity<CalendarResponse> getEvent(@PathVariable Long id) {
        CalendarResponse response = calendarService.getEventById(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/calendar/{id}")
    public ResponseEntity<CalendarResponse> updateEvent(@PathVariable Long id,
                                                        @RequestBody CalendarRequest request) {
        CalendarResponse response = calendarService.updateEvent(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/calendar/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        calendarService.deleteEvent(id);
        return ResponseEntity.noContent().build(); // HTTP 204
    }
}
