package com.swyp.wedding.service.likes;

import com.swyp.wedding.dto.weddinghall.WeddingHallRequest;

public interface LikesService {
    boolean storeLikes(String likesType, Long postId, String userId);
    boolean deleteLikes(Long id);
    boolean deleteLikesByPost(String category, Long postId, String userId);
}
