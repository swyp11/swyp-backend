package com.swyp.wedding.service.likes;

import com.swyp.wedding.dto.weddinghall.WeddingHallRequest;

public interface LikesService {
    boolean storeLikes(String likesType, Long postId, String userId);
}
