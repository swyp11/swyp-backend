package com.swyp.wedding.service.hall;

import java.util.List;

import com.swyp.wedding.dto.hall.HallRequest;
import com.swyp.wedding.dto.hall.HallResponse;
import com.swyp.wedding.dto.weddinghall.WeddingHallRequest;
import com.swyp.wedding.dto.weddinghall.WeddingHallResponse;

public interface HallService {
    List<HallResponse> getHallInfos();
    boolean saveHall(HallRequest request);
    HallResponse getHallInfo(Long id);
    boolean updateHall(HallRequest request);
    boolean deleteHall(Long id);
}
