package com.swyp.wedding.service.dress;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.swyp.wedding.dto.dress.DressRequest;
import com.swyp.wedding.dto.dress.DressResponse;
import com.swyp.wedding.entity.dress.Dress;
import com.swyp.wedding.repository.dress.DressRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DressService {

    private final DressRepository dressRepository;

    // 전체 드레스 목록 조회
    public List<DressResponse> getAllDresses() {
        return dressRepository.findAll()
                .stream()
                .map(DressResponse::from)
                .collect(Collectors.toList());
    }

    // 특정 드레스 조회
    public DressResponse getDressById(Long id) {
        Dress dress = dressRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("드레스를 찾을 수 없습니다. ID: " + id));
        return DressResponse.from(dress);
    }

    // 새 드레스 생성
    @Transactional
    public DressResponse createDress(DressRequest request) {
        Dress dress = request.toEntity();
        Dress savedDress = dressRepository.save(dress);
        return DressResponse.from(savedDress);
    }

    // 드레스 정보 수정
    @Transactional
    public DressResponse updateDress(Long id, DressRequest request) {
        Dress existingDress = dressRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("드레스를 찾을 수 없습니다. ID: " + id));

        // 기존 드레스 정보 업데이트
        Dress updatedDress = Dress.builder()
                .id(existingDress.getId())  // 기존 ID 유지
                .name(request.getName())
                .color(request.getColor())
                .shape(request.getShape())
                .priceRange(request.getPriceRange())
                .length(request.getLength())
                .season(request.getSeason())
                .brand(request.getBrand())
                .designer(request.getDesigner())
                .type(request.getType())
                .neckLine(request.getNeckLine())
                .mood(request.getMood())
                .fabric(request.getFabric())
                .regDt(existingDress.getRegDt())  // 생성 시간은 유지
                .build();

        Dress savedDress = dressRepository.save(updatedDress);
        return DressResponse.from(savedDress);
    }

    // 드레스 삭제
    @Transactional
    public void deleteDress(Long id) {
        if (!dressRepository.existsById(id)) {
            throw new RuntimeException("드레스를 찾을 수 없습니다. ID: " + id);
        }
        dressRepository.deleteById(id);
    }

    // 드레스 총 개수 조회
    public long getDressCount() {
        return dressRepository.count();
    }
}