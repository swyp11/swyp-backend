package com.swyp.wedding.dto.likes;

import com.swyp.wedding.entity.hall.Hall;
import com.swyp.wedding.entity.likes.Likes;
import com.swyp.wedding.entity.likes.LikesType;
import com.swyp.wedding.entity.user.User;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Comment;

import java.time.LocalDateTime;

@Data
public class LikesRequest {
    private Long id;
    private User user;
    private LikesType likesType;
    private Long targetId;
    private LocalDateTime updateDt;
}
