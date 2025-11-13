package com.swyp.wedding.service.weddinghall;

import java.util.List;

import com.swyp.wedding.dto.weddinghall.WeddingHallRequest;
import com.swyp.wedding.dto.weddinghall.WeddingHallResponse;
import com.swyp.wedding.entity.common.SortType;

public interface WeddingHallService {
    List<WeddingHallResponse> getWeddingInfos(SortType sort, String userId);
    boolean saveWedding(WeddingHallRequest request);
    WeddingHallResponse getWeddingInfo(Long id, String userId);
    boolean updateWedding(WeddingHallRequest request);
    boolean deleteWedding(Long id);
    List<WeddingHallResponse> searchWeddings(String keyword);
}
