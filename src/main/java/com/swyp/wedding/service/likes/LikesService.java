package com.swyp.wedding.service.likes;

import com.swyp.wedding.dto.likes.LikesResponse;

public interface LikesService {
    LikesResponse storeLikes(String likesType, Long postId, String userId);
    boolean deleteLikes(Long id);
    LikesResponse deleteLikesByPost(String category, Long postId, String userId);
}
