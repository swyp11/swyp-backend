package com.swyp.wedding.controller.hall;

import java.util.List;
import java.util.Map;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.swyp.wedding.dto.hall.HallRequest;
import com.swyp.wedding.dto.hall.HallResponse;
import com.swyp.wedding.service.hall.impl.HallServiceImpl;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

@Tag(name = "웨딩홀 - 홀 API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/hall")
public class HallApiController {

    private final HallServiceImpl hallService;

    @Operation(summary = "홀 리스트를 조회합니다.")
    @GetMapping
    public ResponseEntity<List<HallResponse>> getHalls() {
        return ResponseEntity.ok(hallService.getHallInfos());
    }


    @Operation(summary = "홀에 대한 상세정보를 조회합니다.")
    @GetMapping("/{id}")
    public ResponseEntity<HallResponse> getHallInfo(@PathVariable("id") Long id) {
        return ResponseEntity.ok(hallService.getHallInfo(id));
    }

    @Operation(summary = "홀에 대한 정보를 저장합니다.")
    @PostMapping
    public ResponseEntity<Map<String, Object>> saveHall(@RequestBody HallRequest request) {
        return ResponseEntity.ok(Map.of("result", hallService.saveHall(request)));
    }

    @Operation(summary = "홀에 대한 정보를 수정합니다.")
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateHall(@PathVariable Long id, @RequestBody HallRequest request) {
        request.setId(id);
        return ResponseEntity.ok(Map.of("result", hallService.updateHall(request)));
    }

    @Operation(summary = "홀에 대한 정보를 삭제합니다.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteHall(@PathVariable Long id) {
        return ResponseEntity.ok(Map.of("result", hallService.deleteHall(id)));
    }
}