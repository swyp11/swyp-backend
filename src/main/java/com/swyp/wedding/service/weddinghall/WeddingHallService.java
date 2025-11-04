package com.swyp.wedding.service.weddinghall;

import com.swyp.wedding.dto.weddinghall.WeddingHallRequest;
import com.swyp.wedding.dto.weddinghall.WeddingHallResponse;
import com.swyp.wedding.entity.weddinghall.WeddingHall;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface WeddingHallService {
    List<WeddingHallResponse> getWeddingInfos();
    WeddingHall saveWedding(WeddingHallRequest request);
    WeddingHallResponse getWeddingInfo(Long id);
}
