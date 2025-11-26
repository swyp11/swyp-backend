package com.swyp.wedding.service.hall.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.swyp.wedding.dto.dress.DressResponse;
import com.swyp.wedding.dto.hall.HallRequest;
import com.swyp.wedding.dto.hall.HallResponse;
import com.swyp.wedding.entity.dress.Dress;
import com.swyp.wedding.entity.hall.Hall;
import com.swyp.wedding.repository.hall.HallRepository;
import com.swyp.wedding.service.hall.HallService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class HallServiceImpl implements HallService {

    private final HallRepository hallRepository;

    @Override
    public List<HallResponse> getHallInfos() {
        List<Hall> infos = hallRepository.findAll();

        return infos.stream()
                .map(HallResponse::from)
                .collect(Collectors.toList());
    }

    @Override
    public boolean saveHall(HallRequest request) {
        if (request == null) {
            return false;
        }
        
        try {
            Hall hall = request.toEntity();
            hallRepository.save(hall);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public HallResponse getHallInfo(Long id) {
        return HallResponse.from(hallRepository.getReferenceById(id));
    }

    @Override
    public boolean updateHall(HallRequest request) {
        if (request == null || request.getId() == null) {
            return false;
        }

        Long id = request.getId();

        return hallRepository.findById(id)
                .map(existing -> {
                    // AllArgsConstructor를 사용하여 새로운 Hall 객체 생성
                    Hall updated = new Hall(
                            id,
                            request.getName(),
                            request.getCapacityMin(),
                            request.getCapacityMax(),
                            request.getHallType(),
                            request.getFloorNo(),
                            request.getLightType(),
                            request.getAreaM2(),
                            request.getCeilingHeight(),
                            request.isStage(),
                            request.isLedWall(),
                            request.getAisleLength(),
                            request.isPillar(),
                            request.isStatus(),
                            request.getDescription(),
                            existing.getRegDt(),  // 기존 등록일 유지
                            null,
                            null  // updateDt는 @PreUpdate에서 자동 설정
                    );
                    hallRepository.save(updated);
                    return true;
                })
                .orElse(false);
    }

    @Override
    public boolean deleteHall(Long id) {
        if (!hallRepository.existsById(id))
            return false;

        try {
            hallRepository.deleteById(id);
            return true;
        } catch (DataIntegrityViolationException e) {
            throw e;
        }
    }


    @Override
    public List<HallResponse> getHallByWeddingHallId(Long weddingHallId) {

        List<Hall> halls;
        halls = hallRepository.findByWeddingHallId(weddingHallId);
        return halls.stream()
                .map(HallResponse::from)
                .collect(Collectors.toList());
    }
}