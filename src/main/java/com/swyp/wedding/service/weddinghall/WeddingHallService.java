package com.swyp.wedding.service.weddinghall;

import java.util.List;

import com.swyp.wedding.dto.weddinghall.WeddingHallRequest;
import com.swyp.wedding.dto.weddinghall.WeddingHallResponse;

public interface WeddingHallService {
    List<WeddingHallResponse> getWeddingInfos();
    boolean saveWedding(WeddingHallRequest request);
    WeddingHallResponse getWeddingInfo(Long id);
    boolean updateWedding(WeddingHallRequest request);
}
