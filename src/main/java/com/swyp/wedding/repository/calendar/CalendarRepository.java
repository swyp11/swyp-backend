package com.swyp.wedding.repository.calendar;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.swyp.wedding.entity.calendar.Calendar;
@Repository
public interface CalendarRepository extends JpaRepository <Calendar, Long>{
}
