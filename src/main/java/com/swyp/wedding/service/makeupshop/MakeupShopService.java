package com.swyp.wedding.service.makeupshop;

import com.swyp.wedding.dto.makeupshop.MakeupShopRequest;
import com.swyp.wedding.dto.makeupshop.MakeupShopResponse;
import com.swyp.wedding.entity.common.SortType;
import com.swyp.wedding.entity.likes.Likes;
import com.swyp.wedding.entity.likes.LikesType;
import com.swyp.wedding.entity.makeupshop.MakeupShop;
import com.swyp.wedding.entity.user.User;
import com.swyp.wedding.repository.likes.LikesRepository;
import com.swyp.wedding.repository.makeupshop.MakeupShopRepository;
import com.swyp.wedding.repository.user.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MakeupShopService {

    private final MakeupShopRepository makeupShopRepository;
    private final LikesRepository likesRepository;
    private final UserRepository userRepository;

    // 전체 메이크업샵 조회
    public List<MakeupShopResponse> getAllMakeupShops() {
        return makeupShopRepository.findAll().stream()
                .map(MakeupShopResponse::from)
                .collect(Collectors.toList());
    }

    // ID로 메이크업샵 조회 (로그인 여부에 따라 찜 정보 포함)
    public MakeupShopResponse getMakeupShopById(Long id, String userId) {
        MakeupShop makeupShop = makeupShopRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("MakeupShop not found with id: " + id));

        MakeupShopResponse response = MakeupShopResponse.from(makeupShop);

        // 로그인한 사용자의 경우 찜 여부 확인
        if (userId != null) {
            User user = userRepository.findByUserId(userId).orElse(null);
            if (user != null) {
                boolean isLiked = likesRepository.existsByUserAndLikesTypeAndTargetId(user, LikesType.MAKEUP_SHOP, id);
                response.setIsLiked(isLiked);
            }
        }

        return response;
    }

    // 새 메이크업샵 생성
    @Transactional
    public MakeupShopResponse createMakeupShop(MakeupShopRequest request) {
        MakeupShop makeupShop = request.toEntity();
        MakeupShop savedMakeupShop = makeupShopRepository.save(makeupShop);
        return MakeupShopResponse.from(savedMakeupShop);
    }

    // 메이크업샵 정보 수정
    @Transactional
    public MakeupShopResponse updateMakeupShop(Long id, MakeupShopRequest request) {
        MakeupShop existingMakeupShop = makeupShopRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("MakeupShop not found with id: " + id));

        MakeupShop updatedMakeupShop = MakeupShop.builder()
                .id(existingMakeupShop.getId())
                .shopName(request.getShopName() != null ? request.getShopName() : existingMakeupShop.getShopName())
                .description(request.getDescription() != null ? request.getDescription() : existingMakeupShop.getDescription())
                .address(request.getAddress() != null ? request.getAddress() : existingMakeupShop.getAddress())
                .phone(request.getPhone() != null ? request.getPhone() : existingMakeupShop.getPhone())
                .snsUrl(request.getSnsUrl() != null ? request.getSnsUrl() : existingMakeupShop.getSnsUrl())
                .imageUrl(request.getImageUrl() != null ? request.getImageUrl() : existingMakeupShop.getImageUrl())
                .specialty(request.getSpecialty() != null ? request.getSpecialty() : existingMakeupShop.getSpecialty())
                .features(request.getFeatures() != null ? request.getFeatures() : existingMakeupShop.getFeatures())
                .regDt(existingMakeupShop.getRegDt())
                .build();

        MakeupShop savedMakeupShop = makeupShopRepository.save(updatedMakeupShop);
        return MakeupShopResponse.from(savedMakeupShop);
    }

    // 메이크업샵 삭제 (하드 삭제)
    @Transactional
    public void deleteMakeupShop(Long id) {
        MakeupShop makeupShop = makeupShopRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("MakeupShop not found with id: " + id));
        makeupShopRepository.delete(makeupShop);
    }

    // 복합 조건 검색 (여러 조건을 동시에 적용) - 로그인 여부에 따라 찜 정보 포함
    public List<MakeupShopResponse> searchMakeupShops(String shopName, String address, String specialty, SortType sort, String userId) {
        List<MakeupShop> makeupShops;

        // 정렬 기준에 따라 전체 조회
        if (sort == SortType.RECENT) {
            makeupShops = makeupShopRepository.findAllByOrderByRegDtDesc();
        } else if (sort == SortType.FAVORITE) {
            // tb_likes 테이블에서 likes_type = 'MAKEUP_SHOP'인 항목들을 집계하여 좋아요가 많은 순서대로 ID 목록 가져오기
            List<Object[]> likesCounts = likesRepository.findTargetIdsByLikesTypeOrderByCountDesc(LikesType.MAKEUP_SHOP);

            // target_id(MakeupShop의 id) 목록 추출
            List<Long> sortedIds = likesCounts.stream()
                    .map(arr -> (Long) arr[0])
                    .collect(Collectors.toList());

            // 전체 MakeupShop 조회 후 좋아요 순서에 맞게 정렬
            List<MakeupShop> allShops = makeupShopRepository.findAll();
            Map<Long, MakeupShop> shopMap = allShops.stream()
                    .collect(Collectors.toMap(MakeupShop::getId, shop -> shop));

            // 좋아요가 있는 샵들을 먼저 정렬된 순서로 추가
            makeupShops = sortedIds.stream()
                    .map(shopMap::get)
                    .filter(shop -> shop != null)
                    .collect(Collectors.toList());

            // 좋아요가 없는 나머지 샵들 추가
            allShops.stream()
                    .filter(shop -> !sortedIds.contains(shop.getId()))
                    .forEach(makeupShops::add);
        } else {
            makeupShops = makeupShopRepository.findAll();
        }

        // 필터링 적용
        List<MakeupShop> filteredShops = makeupShops.stream()
                .filter(shop -> shopName == null || shopName.trim().isEmpty() ||
                        shop.getShopName().toLowerCase().contains(shopName.toLowerCase()))
                .filter(shop -> address == null || address.trim().isEmpty() ||
                        (shop.getAddress() != null && shop.getAddress().toLowerCase().contains(address.toLowerCase())))
                .filter(shop -> specialty == null || specialty.trim().isEmpty() ||
                        (shop.getSpecialty() != null && shop.getSpecialty().toLowerCase().contains(specialty.toLowerCase())))
                .collect(Collectors.toList());

        // 로그인한 사용자의 경우 찜 정보 추가
        Set<Long> likedShopIds = Set.of();
        if (userId != null) {
            User user = userRepository.findByUserId(userId).orElse(null);
            if (user != null) {
                List<Long> shopIds = filteredShops.stream()
                        .map(MakeupShop::getId)
                        .collect(Collectors.toList());

                likedShopIds = likesRepository.findByUserAndLikesTypeAndTargetIdIn(user, LikesType.MAKEUP_SHOP, shopIds)
                        .stream()
                        .map(Likes::getTargetId)
                        .collect(Collectors.toSet());
            }
        }

        // Response 변환 시 찜 정보 포함
        final Set<Long> finalLikedShopIds = likedShopIds;
        return filteredShops.stream()
                .map(shop -> {
                    MakeupShopResponse response = MakeupShopResponse.from(shop);
                    response.setIsLiked(userId != null ? finalLikedShopIds.contains(shop.getId()) : null);
                    return response;
                })
                .collect(Collectors.toList());
    }
}
