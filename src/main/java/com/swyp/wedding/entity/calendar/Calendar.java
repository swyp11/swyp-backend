package com.swyp.wedding.entity.calendar;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tb_calendar")
@Builder
@Getter
@EntityListeners(AuditingEntityListener.class) // Auditing 활성화 포인트
public class Calendar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    @Column(columnDefinition = "TEXT")
    private String memo;

    private LocalDateTime startTime; // 날짜 형식 2025-11-05 10:30:00
    private LocalDateTime endTime;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt; // 생성 시 자동 입력

    @LastModifiedDate
    private LocalDateTime updatedAt; // 수정 시 자동 갱신

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "user_id")
//    private User user; // 로그인한 사용자 정보

}
