package com.swyp.wedding.entity.likes;

import com.swyp.wedding.entity.user.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "tb_likes")
public class Likes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Comment("찜을 한 카테고리 유형")
    @Enumerated(EnumType.STRING)
    @Column(name = "likes_type")
    private LikesType likesType;

    @Comment("해당 카테고리 식별자")
    @Column(name = "target_id")
    private Long targetId;

    @Comment("업데이트 일자")
    @Column(name = "update_dt")
    private LocalDateTime updateDt;

    @PrePersist
    protected void onCreate() {
        updateDt = LocalDateTime.now();
    }

    @Builder
    public Likes(User user, LikesType likesType, Long targetId) {
        this.user = user;
        this.likesType = likesType;
        this.targetId = targetId;
    }
}
