package com.swyp.wedding.service.weddinghall;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.swyp.wedding.dto.weddinghall.WeddingHallRequest;
import com.swyp.wedding.dto.weddinghall.WeddingHallResponse;
import com.swyp.wedding.entity.weddinghall.WeddingHall;
import com.swyp.wedding.repository.weddinghall.WeddingHallRepository;

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
                    WeddingHall updated = new WeddingHall(
                            id,
                            request.getName(),
                            request.getVenueType(),
                            request.getParking(),
                            request.getAddress(),
                            request.getPhone(),
                            request.getEmail()
                    );
                    weddingHallRepository.save(updated);
                    return true;
                })
                .orElse(false);
    }
}
