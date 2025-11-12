package com.swyp.wedding.service.makeupshop;

import com.swyp.wedding.dto.makeupshop.MakeupShopRequest;
import com.swyp.wedding.dto.makeupshop.MakeupShopResponse;
import com.swyp.wedding.entity.common.SortType;
import com.swyp.wedding.entity.likes.LikesType;
import com.swyp.wedding.entity.makeupshop.MakeupShop;
import com.swyp.wedding.repository.likes.LikesRepository;
import com.swyp.wedding.repository.makeupshop.MakeupShopRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MakeupShopService {

    private final MakeupShopRepository makeupShopRepository;
    private final LikesRepository likesRepository;

    // 전체 메이크업샵 조회
    public List<MakeupShopResponse> getAllMakeupShops() {
        return makeupShopRepository.findAll().stream()
                .map(MakeupShopResponse::from)
                .collect(Collectors.toList());
    }

    // ID로 메이크업샵 조회
    public MakeupShopResponse getMakeupShopById(Long id) {
        MakeupShop makeupShop = makeupShopRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("MakeupShop not found with id: " + id));
        return MakeupShopResponse.from(makeupShop);
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

    // 복합 조건 검색 (여러 조건을 동시에 적용)
    public List<MakeupShopResponse> searchMakeupShops(String shopName, String address, String specialty, SortType sort) {
        List<MakeupShop> makeupShops;

        // 정렬 기준에 따라 전체 조회
        if (sort == SortType.RECENT) {
            makeupShops = makeupShopRepository.findAllByOrderByRegDtDesc();
        } else {
            makeupShops = makeupShopRepository.findAll();
        }

        // 필터링 적용 (스트림으로 여러 조건 동시 적용)
        return makeupShops.stream()
                .filter(shop -> shopName == null || shopName.trim().isEmpty() ||
                        shop.getShopName().toLowerCase().contains(shopName.toLowerCase()))
                .filter(shop -> address == null || address.trim().isEmpty() ||
                        (shop.getAddress() != null && shop.getAddress().toLowerCase().contains(address.toLowerCase())))
                .filter(shop -> specialty == null || specialty.trim().isEmpty() ||
                        (shop.getSpecialty() != null && shop.getSpecialty().toLowerCase().contains(specialty.toLowerCase())))
                .map(MakeupShopResponse::from)
                .collect(Collectors.toList());
    }
}
