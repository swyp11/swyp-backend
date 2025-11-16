package com.swyp.wedding.entity.schedule;

import com.swyp.wedding.dto.schedule.ScheduleRequest;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import com.swyp.wedding.entity.user.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tb_schedule")
@Builder
@Getter
@EntityListeners(AuditingEntityListener.class) // Auditing 활성화 포인트
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    @Column(columnDefinition = "TEXT")
    private String memo;

    private LocalTime startTime; // 날짜 형식 2025-11-05 10:30:00
    private LocalTime endTime;

    private LocalDate startDate;
    private LocalDate endDate;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt; // 생성 시 자동 입력

    @LastModifiedDate
    private LocalDateTime updatedAt; // 수정 시 자동 갱신

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user; // 로그인한 사용자 정보

    @Builder.Default
    private Integer notifyBeforeMinutes = 10;

    private LocalDateTime notifiedAt;

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public void markNotified() {
        this.notifiedAt = LocalDateTime.now();
    }

    public void update(ScheduleRequest request){
        this.title = request.getTitle();
        this.startDate = request.getStartDate();
        this.endDate = request.getEndDate();
        this.startTime = request.getStartTime();
        this.endTime = request.getEndTime();
        this.memo = request.getMemo();
    }


}
