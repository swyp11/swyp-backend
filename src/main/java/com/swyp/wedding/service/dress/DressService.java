package com.swyp.wedding.service.dress;

import com.swyp.wedding.global.exception.BusinessException;
import com.swyp.wedding.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.swyp.wedding.dto.dress.DressRequest;
import com.swyp.wedding.dto.dress.DressResponse;
import com.swyp.wedding.entity.common.SortType;
import com.swyp.wedding.entity.dress.Dress;
import com.swyp.wedding.entity.likes.LikesType;
import com.swyp.wedding.repository.dress.DressRepository;
import com.swyp.wedding.repository.likes.LikesRepository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DressService {

    private final DressRepository dressRepository;
    private final LikesRepository likesRepository;

    // 특정 드레스 조회
    public DressResponse getDressById(Long id) {
        Dress dress = dressRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.DRESS_NOT_FOUND));
        return DressResponse.from(dress);
    }

    // 새 드레스 생성
    @Transactional
    public DressResponse createDress(DressRequest request) {
        // shopName 기본 검증
        if (request.getShopName() == null || request.getShopName().trim().isEmpty()) {
            throw new BusinessException(ErrorCode.INVALID_INPUT_VALUE, "Shop Name은 필수입니다.");
        }

        Dress dress = request.toEntity();
        Dress savedDress = dressRepository.save(dress);
        return DressResponse.from(savedDress);
    }

    // 드레스 정보 수정
    @Transactional
    public DressResponse updateDress(Long id, DressRequest request) {
        Dress existingDress = dressRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.DRESS_NOT_FOUND));

        // 기존 드레스 정보 업데이트
        Dress updatedDress = Dress.builder()
                .id(existingDress.getId())  // 기존 ID 유지
                .name(request.getName())
                .color(request.getColor())
                .shape(request.getShape())
                .priceRange(request.getPriceRange())
                .length(request.getLength())
                .season(request.getSeason())
                .shopName(request.getShopName() != null ? request.getShopName() : existingDress.getShopName())
                .designer(request.getDesigner())
                .type(request.getType())
                .neckLine(request.getNeckLine())
                .mood(request.getMood())
                .fabric(request.getFabric())
                .features(request.getFeatures())
                .regDt(existingDress.getRegDt())  // 생성 시간은 유지
                .build();

        Dress savedDress = dressRepository.save(updatedDress);
        return DressResponse.from(savedDress);
    }

    // 드레스 삭제
    @Transactional
    public void deleteDress(Long id) {
        if (!dressRepository.existsById(id)) {
            throw new BusinessException(ErrorCode.DRESS_NOT_FOUND);
        }
        dressRepository.deleteById(id);
    }
    
    // shopName으로 드레스들 조회 (정확한 이름 일치) - DressShopController용
    public List<DressResponse> getDressesByShopName(String shopName) {
        List<Dress> dresses = dressRepository.findByShopName(shopName);
        return dresses.stream()
                .map(DressResponse::from)
                .collect(Collectors.toList());
    }
    
    // 복합 조건 검색 (여러 조건을 동시에 적용)
    public List<DressResponse> searchDresses(String shopNameContains, SortType sort) {
        List<Dress> dresses;
        
        // 정렬 기준에 따라 전체 조회
        if (sort == SortType.RECENT) {
            dresses = dressRepository.findAllByOrderByRegDtDesc();
        } else if (sort == SortType.FAVORITE) {
            // tb_likes 테이블에서 likes_type = 'DRESS'인 항목들을 집계하여 좋아요가 많은 순서대로 ID 목록 가져오기
            List<Object[]> likesCounts = likesRepository.findTargetIdsByLikesTypeOrderByCountDesc(LikesType.DRESS);
            
            // target_id(Dress의 id) 목록 추출
            List<Long> sortedIds = likesCounts.stream()
                    .map(arr -> (Long) arr[0])
                    .collect(Collectors.toList());
            
            // 전체 Dress 조회 후 좋아요 순서에 맞게 정렬
            List<Dress> allDresses = dressRepository.findAll();
            Map<Long, Dress> dressMap = allDresses.stream()
                    .collect(Collectors.toMap(Dress::getId, dress -> dress));
            
            // 좋아요가 있는 드레스들을 먼저 정렬된 순서로 추가
            dresses = sortedIds.stream()
                    .map(dressMap::get)
                    .filter(dress -> dress != null)
                    .collect(Collectors.toList());
            
            // 좋아요가 없는 나머지 드레스들 추가
            allDresses.stream()
                    .filter(dress -> !sortedIds.contains(dress.getId()))
                    .forEach(dresses::add);
        } else {
            dresses = dressRepository.findAll();
        }
        
        // 필터링 적용 (스트림으로 여러 조건 동시 적용)
        return dresses.stream()
                .filter(dress -> {
                    if (shopNameContains != null && !shopNameContains.trim().isEmpty()) {
                        // 부분 일치
                        return dress.getShopName().toLowerCase().contains(shopNameContains.toLowerCase());
                    }
                    return true;
                })
                .map(DressResponse::from)
                .collect(Collectors.toList());
    }

    public List<DressResponse> getDressesByShopId(Long shopId) {

        List<Dress> dresses;
        dresses = dressRepository.findByDressShopId(shopId);
        return dresses.stream()
                .map(DressResponse::from)
                .collect(Collectors.toList());
    }
}