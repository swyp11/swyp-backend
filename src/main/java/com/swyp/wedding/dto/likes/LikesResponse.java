package com.swyp.wedding.dto.likes;

import com.swyp.wedding.entity.likes.Likes;
import com.swyp.wedding.entity.likes.LikesType;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class LikesResponse {
    private Long id;
    private LikesType likesType;
    private Long targetId;
    private LocalDateTime updateDt;
    private Object itemDetails;  // 실제 아이템 정보 (드레스샵, 웨딩홀 등)

    public static LikesResponse from(Likes likes) {
        return LikesResponse.builder()
                .id(likes.getId())
                .likesType(likes.getLikesType())
                .targetId(likes.getTargetId())
                .updateDt(likes.getUpdateDt())
                .build();
    }
}
