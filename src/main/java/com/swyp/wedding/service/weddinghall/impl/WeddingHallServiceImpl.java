package com.swyp.wedding.service.weddinghall.impl;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.swyp.wedding.dto.weddinghall.WeddingHallRequest;
import com.swyp.wedding.dto.weddinghall.WeddingHallResponse;
import com.swyp.wedding.entity.common.SortType;
import com.swyp.wedding.entity.likes.Likes;
import com.swyp.wedding.entity.likes.LikesType;
import com.swyp.wedding.entity.user.User;
import com.swyp.wedding.entity.weddinghall.WeddingHall;
import com.swyp.wedding.repository.likes.LikesRepository;
import com.swyp.wedding.repository.user.UserRepository;
import com.swyp.wedding.repository.weddinghall.WeddingHallRepository;
import com.swyp.wedding.service.weddinghall.WeddingHallService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class WeddingHallServiceImpl implements WeddingHallService{

    final private WeddingHallRepository weddingHallRepository;
    final private LikesRepository likesRepository;
    final private UserRepository userRepository;

    @Override
    public List<WeddingHallResponse> getWeddingInfos(SortType sort, String userId) {
        List<WeddingHall> infos;

        if (sort == SortType.FAVORITE) {
            // 인기순 정렬 (찜 개수 기준)
            infos = weddingHallRepository.findAllOrderByLikesCountDesc();
        } else {
            // 최신순 정렬 (기본값)
            infos = weddingHallRepository.findAllByOrderByRegDtDesc();
        }

        // 로그인한 사용자의 경우 찜 정보 추가
        Set<Long> likedHallIds = Set.of();
        if (userId != null) {
            User user = userRepository.findByUserId(userId).orElse(null);
            if (user != null) {
                List<Long> hallIds = infos.stream()
                        .map(WeddingHall::getId)
                        .collect(Collectors.toList());

                likedHallIds = likesRepository.findByUserAndLikesTypeAndTargetIdIn(user, LikesType.WEDDING_HALL, hallIds)
                        .stream()
                        .map(Likes::getTargetId)
                        .collect(Collectors.toSet());
            }
        }

        // Response 변환 시 찜 정보 포함
        final Set<Long> finalLikedHallIds = likedHallIds;
        return infos.stream()
                .map(hall -> {
                    WeddingHallResponse response = WeddingHallResponse.from(hall);
                    response.setIsLiked(userId != null ? finalLikedHallIds.contains(hall.getId()) : null);
                    return response;
                })
                .collect(Collectors.toList());
    }

    @Override
    public boolean saveWedding(WeddingHallRequest request) {
        if (request == null) {
            return false;
        }
        
        try {
            WeddingHall weddingHall = request.toEntity();
            weddingHallRepository.save(weddingHall);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public WeddingHallResponse getWeddingInfo(Long id, String userId) {
        WeddingHall weddingHall = weddingHallRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("WeddingHall not found with id: " + id));

        WeddingHallResponse response = WeddingHallResponse.from(weddingHall);

        // 로그인한 사용자의 경우 찜 여부 확인
        if (userId != null) {
            User user = userRepository.findByUserId(userId).orElse(null);
            if (user != null) {
                boolean isLiked = likesRepository.existsByUserAndLikesTypeAndTargetId(user, LikesType.WEDDING_HALL, id);
                response.setIsLiked(isLiked);
            }
        }

        return response;
    }

    @Override
    public boolean updateWedding(WeddingHallRequest request) {
        if (request == null || request.getId() == null) {
            return false;
        }

        Long id = request.getId();

        return weddingHallRepository.findById(id)
                .map(existing -> {
                    WeddingHall updated = WeddingHall.builder()
                            .id(id)
                            .name(request.getName())
                            .venueType(request.getVenueType())
                            .parking(request.getParking())
                            .address(request.getAddress())
                            .phone(request.getPhone())
                            .email(request.getEmail())
                            .imageUrl(request.getImageUrl())
                            .build();
                    weddingHallRepository.save(updated);
                    return true;
                })
                .orElse(false);
    }

    @Override
    public boolean deleteWedding(Long id) {
        if (!weddingHallRepository.existsById(id))
            return false;

        try{
            weddingHallRepository.deleteById(id);
            return true;
        } catch (DataIntegrityViolationException e) {
            throw e;
        }
    }
}
