package com.swyp.wedding.service.weddinghall;

import com.swyp.wedding.dto.weddinghall.WeddingHallRequest;
import com.swyp.wedding.dto.weddinghall.WeddingHallResponse;
import com.swyp.wedding.entity.weddinghall.WeddingHall;
import com.swyp.wedding.repository.weddinghall.WeddingHallRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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
    public WeddingHall saveWedding(WeddingHallRequest request) {
        WeddingHall weddingHall = request.toEntity();

        return weddingHallRepository.save(weddingHall);
    }

    @Override
    public WeddingHallResponse getWeddingInfo(Long id) {
        return WeddingHallResponse.from(weddingHallRepository.getReferenceById(id));
    }
}
