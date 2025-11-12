package com.swyp.wedding.service.likes.impl;

import com.swyp.wedding.dto.dressshop.DressShopResponse;
import com.swyp.wedding.dto.likes.LikesResponse;
import com.swyp.wedding.dto.makeupshop.MakeupShopResponse;
import com.swyp.wedding.dto.weddinghall.WeddingHallResponse;
import com.swyp.wedding.entity.dressshop.DressShop;
import com.swyp.wedding.entity.likes.Likes;
import com.swyp.wedding.entity.likes.LikesType;
import com.swyp.wedding.entity.makeupshop.MakeupShop;
import com.swyp.wedding.entity.user.User;
import com.swyp.wedding.entity.weddinghall.WeddingHall;
import com.swyp.wedding.repository.dressshop.DressShopRepository;
import com.swyp.wedding.repository.hall.HallRepository;
import com.swyp.wedding.repository.makeupshop.MakeupShopRepository;
import com.swyp.wedding.repository.user.UserRepository;
import com.swyp.wedding.repository.weddinghall.WeddingHallRepository;
import com.swyp.wedding.service.hall.HallService;
import com.swyp.wedding.service.likes.LikesService;

import lombok.RequiredArgsConstructor;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.swyp.wedding.repository.likes.LikesRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class LikesServiceImpl implements LikesService {

    private final HallRepository hallRepository;
    private final UserRepository userRepository;
    private final LikesRepository likesRepository;
    private final DressShopRepository dressShopRepository;
    private final MakeupShopRepository makeupShopRepository;
    private final WeddingHallRepository weddingHallRepository;

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
                case "dress_shop" -> likesType = LikesType.DRESS_SHOP;
                case "makeup_shop" -> likesType = LikesType.MAKEUP_SHOP;
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
    @Override
    public boolean deleteLikes(Long id) {
        // 없는 정보에 대한 예외처리 만들 예정 (고도화 때)
         if (!likesRepository.existsById(id))
            return false;

        try {
            likesRepository.deleteById(id);
            return true;
        } catch (DataIntegrityViolationException e) {
            return false;
        }
    }

    // 사용자의 모든 찜 목록 조회
    public List<LikesResponse> getUserLikes(String userId) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        List<Likes> likesList = likesRepository.findByUserOrderByUpdateDtDesc(user);

        return likesList.stream()
                .map(this::mapToLikesResponseWithDetails)
                .collect(Collectors.toList());
    }

    // 사용자의 카테고리별 찜 목록 조회
    public List<LikesResponse> getUserLikesByCategory(String userId, String category) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        LikesType likesType = switch (category.toLowerCase()) {
            case "hall" -> LikesType.HALL;
            case "wedding_hall" -> LikesType.WEDDING_HALL;
            case "dress" -> LikesType.DRESS;
            case "dress_shop" -> LikesType.DRESS_SHOP;
            case "makeup_shop" -> LikesType.MAKEUP_SHOP;
            default -> throw new IllegalArgumentException("지원하지 않는 카테고리입니다: " + category);
        };

        List<Likes> likesList = likesRepository.findByUserAndLikesTypeOrderByUpdateDtDesc(user, likesType);

        return likesList.stream()
                .map(this::mapToLikesResponseWithDetails)
                .collect(Collectors.toList());
    }

    // Likes를 LikesResponse로 변환 (실제 아이템 정보 포함)
    private LikesResponse mapToLikesResponseWithDetails(Likes likes) {
        LikesResponse response = LikesResponse.from(likes);

        // 카테고리별로 실제 아이템 정보 조회
        Object itemDetails = switch (likes.getLikesType()) {
            case DRESS_SHOP -> {
                DressShop dressShop = dressShopRepository.findById(likes.getTargetId()).orElse(null);
                if (dressShop != null) {
                    DressShopResponse dressShopResponse = DressShopResponse.from(dressShop);
                    dressShopResponse.setIsLiked(true);
                    yield dressShopResponse;
                }
                yield null;
            }
            case MAKEUP_SHOP -> {
                MakeupShop makeupShop = makeupShopRepository.findById(likes.getTargetId()).orElse(null);
                if (makeupShop != null) {
                    MakeupShopResponse makeupShopResponse = MakeupShopResponse.from(makeupShop);
                    makeupShopResponse.setIsLiked(true);
                    yield makeupShopResponse;
                }
                yield null;
            }
            case WEDDING_HALL -> {
                WeddingHall weddingHall = weddingHallRepository.findById(likes.getTargetId()).orElse(null);
                if (weddingHall != null) {
                    WeddingHallResponse weddingHallResponse = WeddingHallResponse.from(weddingHall);
                    weddingHallResponse.setIsLiked(true);
                    yield weddingHallResponse;
                }
                yield null;
            }
            default -> null;
        };

        response.setItemDetails(itemDetails);
        return response;
    }
}
