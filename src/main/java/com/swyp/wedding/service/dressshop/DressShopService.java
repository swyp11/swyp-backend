package com.swyp.wedding.service.dressshop;

import com.swyp.wedding.dto.dressshop.DressShopRequest;
import com.swyp.wedding.dto.dressshop.DressShopResponse;
import com.swyp.wedding.entity.common.SortType;
import com.swyp.wedding.entity.dressshop.DressShop;
import com.swyp.wedding.entity.likes.LikesType;
import com.swyp.wedding.repository.dressshop.DressShopRepository;
import com.swyp.wedding.repository.likes.LikesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DressShopService {

    private final DressShopRepository dressShopRepository;
    private final LikesRepository likesRepository;

    // 전체 드레스샵 조회
    public List<DressShopResponse> getAllDressShops() {
        return dressShopRepository.findAll().stream()
                .map(DressShopResponse::from)
                .collect(Collectors.toList());
    }
    // ID로 드레스샵 조회
    public DressShopResponse getDressShopById(Long id) {
        DressShop dressShop = dressShopRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("DressShop not found with id: " + id));
        return DressShopResponse.from(dressShop);
    }

    // 새 드레스샵 생성
    @Transactional
    public DressShopResponse createDressShop(DressShopRequest request) {
        DressShop dressShop = request.toEntity();
        DressShop savedDressShop = dressShopRepository.save(dressShop);
        return DressShopResponse.from(savedDressShop);
    }

    // 드레스샵 정보 수정
    @Transactional
    public DressShopResponse updateDressShop(Long id, DressShopRequest request) {
        DressShop existingDressShop = dressShopRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("DressShop not found with id: " + id));

        DressShop updatedDressShop = DressShop.builder()
                .id(existingDressShop.getId())
                .shopName(request.getShopName() != null ? request.getShopName() : existingDressShop.getShopName())
                .description(request.getDescription() != null ? request.getDescription() : existingDressShop.getDescription())
                .address(request.getAddress() != null ? request.getAddress() : existingDressShop.getAddress())
                .phone(request.getPhone() != null ? request.getPhone() : existingDressShop.getPhone())
                .snsUrl(request.getSnsUrl() != null ? request.getSnsUrl() : existingDressShop.getSnsUrl())
                .imageUrl(request.getImageUrl() != null ? request.getImageUrl() : existingDressShop.getImageUrl())
                .specialty(request.getSpecialty() != null ? request.getSpecialty() : existingDressShop.getSpecialty())
                .features(request.getFeatures() != null ? request.getFeatures() : existingDressShop.getFeatures())
                .regDt(existingDressShop.getRegDt())
                .build();

        DressShop savedDressShop = dressShopRepository.save(updatedDressShop);
        return DressShopResponse.from(savedDressShop);
    }

    // 드레스샵 삭제 (하드 삭제)
    @Transactional
    public void deleteDressShop(Long id) {
        DressShop dressShop = dressShopRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("DressShop not found with id: " + id));
        dressShopRepository.delete(dressShop);
    }
    
    // 복합 조건 검색 (여러 조건을 동시에 적용)
    public List<DressShopResponse> searchDressShops(String shopName, String address, String specialty, SortType sort) {
        List<DressShop> dressShops;
        
        // 정렬 기준에 따라 전체 조회
        if (sort == SortType.RECENT) {
            dressShops = dressShopRepository.findAllByOrderByRegDtDesc();
        } else if (sort == SortType.FAVORITE) {
            // tb_likes 테이블에서 likes_type = 'SHOP'인 항목들을 집계하여 좋아요가 많은 순서대로 ID 목록 가져오기
            List<Object[]> likesCounts = likesRepository.findTargetIdsByLikesTypeOrderByCountDesc(LikesType.SHOP);
            
            // target_id(DressShop의 id) 목록 추출
            List<Long> sortedIds = likesCounts.stream()
                    .map(arr -> (Long) arr[0])
                    .collect(Collectors.toList());
            
            // 전체 DressShop 조회 후 좋아요 순서에 맞게 정렬
            List<DressShop> allShops = dressShopRepository.findAll();
            Map<Long, DressShop> shopMap = allShops.stream()
                    .collect(Collectors.toMap(DressShop::getId, shop -> shop));
            
            // 좋아요가 있는 샵들을 먼저 정렬된 순서로 추가
            dressShops = sortedIds.stream()
                    .map(shopMap::get)
                    .filter(shop -> shop != null)
                    .collect(Collectors.toList());
            
            // 좋아요가 없는 나머지 샵들 추가
            allShops.stream()
                    .filter(shop -> !sortedIds.contains(shop.getId()))
                    .forEach(dressShops::add);
        } else {
            dressShops = dressShopRepository.findAll();
        }
        
        // 필터링 적용 (스트림으로 여러 조건 동시 적용)
        return dressShops.stream()
                .filter(shop -> shopName == null || shopName.trim().isEmpty() || 
                        shop.getShopName().toLowerCase().contains(shopName.toLowerCase()))
                .filter(shop -> address == null || address.trim().isEmpty() || 
                        (shop.getAddress() != null && shop.getAddress().toLowerCase().contains(address.toLowerCase())))
                .filter(shop -> specialty == null || specialty.trim().isEmpty() || 
                        (shop.getSpecialty() != null && shop.getSpecialty().toLowerCase().contains(specialty.toLowerCase())))
                .map(DressShopResponse::from)
                .collect(Collectors.toList());
    }}