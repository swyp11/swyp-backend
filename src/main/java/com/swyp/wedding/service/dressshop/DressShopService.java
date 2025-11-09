package com.swyp.wedding.service.dressshop;

import com.swyp.wedding.dto.dressshop.DressShopRequest;
import com.swyp.wedding.dto.dressshop.DressShopResponse;
import com.swyp.wedding.entity.common.SortType;
import com.swyp.wedding.entity.dressshop.DressShop;
import com.swyp.wedding.repository.dressshop.DressShopRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DressShopService {

    private final DressShopRepository dressShopRepository;

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
            // TODO: 추후 인기순/즐겨찾기순 정렬 로직 구현
            // 현재는 기본 정렬로 처리
            dressShops = dressShopRepository.findAll();
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
    }

}