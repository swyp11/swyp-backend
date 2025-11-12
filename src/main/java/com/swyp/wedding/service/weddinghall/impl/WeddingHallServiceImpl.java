package com.swyp.wedding.service.weddinghall.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.swyp.wedding.dto.weddinghall.WeddingHallRequest;
import com.swyp.wedding.dto.weddinghall.WeddingHallResponse;
import com.swyp.wedding.entity.weddinghall.WeddingHall;
import com.swyp.wedding.repository.weddinghall.WeddingHallRepository;
import com.swyp.wedding.service.weddinghall.WeddingHallService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class WeddingHallServiceImpl implements WeddingHallService{

    final private WeddingHallRepository weddingHallRepository;

    @Override
    public List<WeddingHallResponse> getWeddingInfos() {
        List<WeddingHall> infos = weddingHallRepository.findAll();

        return infos.stream()
                .map(WeddingHallResponse::from)
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
    public WeddingHallResponse getWeddingInfo(Long id) {
        return WeddingHallResponse.from(weddingHallRepository.getReferenceById(id));
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
