package com.swyp.wedding.service.likes.impl;

import com.swyp.wedding.entity.likes.Likes;
import com.swyp.wedding.entity.likes.LikesType;
import com.swyp.wedding.entity.user.User;
import com.swyp.wedding.repository.hall.HallRepository;
import com.swyp.wedding.repository.user.UserRepository;
import com.swyp.wedding.service.hall.HallService;
import com.swyp.wedding.service.likes.LikesService;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import com.swyp.wedding.repository.likes.LikesRepository;

@RequiredArgsConstructor
@Service
public class LikesServiceImpl implements LikesService {

    private final HallRepository hallRepository;
    private final UserRepository userRepository;
    private final LikesRepository likesRepository;

    // 해당 찜 누를 시, 좋아요 저장된다.
    @Override
    public boolean storeLikes(String category, Long postId, String userId) {

        try {
            User user = userRepository.findByUserId(userId).get();
            LikesType likesType = null;

            switch (category) {
                case "hall" -> likesType = LikesType.HALL;
                case "wedding_hall" -> likesType = LikesType.WEDDING_HALL;
                case "dress" -> likesType = LikesType.DRESS;
                case "shop" -> likesType = LikesType.SHOP;
                default -> throw new IllegalArgumentException("지원하지 않는 카테고리입니다: " + category);
            }

            Likes likes = Likes.builder()
                    .user(user)
                    .likesType(likesType)
                    .targetId(postId)
                    .build();

            likesRepository.save(likes);

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // 해당 찜 취소
}
