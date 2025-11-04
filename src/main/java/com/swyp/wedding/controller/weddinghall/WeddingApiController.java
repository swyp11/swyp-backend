package com.swyp.wedding.controller.weddinghall;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.swyp.wedding.dto.weddinghall.WeddingHallRequest;
import com.swyp.wedding.dto.weddinghall.WeddingHallResponse;
import com.swyp.wedding.service.weddinghall.WeddingHallServiceImpl;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/wedding")
public class WeddingApiController {

    private final WeddingHallServiceImpl weddingHallService;

    @Operation(summary = "웨딩홀 리스트를 조회합니다.")
    @GetMapping
    public ResponseEntity<List<WeddingHallResponse>> getWeddings() {
        return ResponseEntity.ok(weddingHallService.getWeddingInfos());
    }


    @Operation(summary = "웨딩홀에 대한 상세정보를 조회합니다.")
    @GetMapping("/{id}")
    public ResponseEntity<WeddingHallResponse> getWeddingInfo(@PathVariable("id") Long id) {
        return ResponseEntity.ok(weddingHallService.getWeddingInfo(id));
    }

    @Operation(summary = "웨딩홀에 대한 정보를 저장합니다.")
    @PostMapping
    public ResponseEntity<Map<String, Object>> saveWedding(@RequestBody WeddingHallRequest request) {
        return ResponseEntity.ok(Map.of("result", weddingHallService.saveWedding(request)));
    }

    @Operation(summary = "웨딩홀에 대한 정보를 수정합니다.")
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateWedding(@PathVariable Long id, @RequestBody WeddingHallRequest request) {
        request.setId(id);
        return ResponseEntity.ok(Map.of("result", weddingHallService.updateWedding(request)));
    }
}
