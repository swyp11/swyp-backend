package com.swyp.wedding.controller.dress;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.swyp.wedding.dto.dress.DressRequest;
import com.swyp.wedding.dto.dress.DressResponse;
import com.swyp.wedding.service.dress.DressService;

import java.util.List;

@RestController
@RequestMapping("/api/dress")
@RequiredArgsConstructor
public class DressController {

    private final DressService dressService;

    // GET /api/dress - 전체 드레스 목록 조회
    @GetMapping
    public ResponseEntity<List<DressResponse>> getAllDresses() {
        List<DressResponse> dresses = dressService.getAllDresses();
        return ResponseEntity.ok(dresses);
    }

    // GET /api/dress/{id} - 특정 드레스 조회
    @GetMapping("/{id}")
    public ResponseEntity<DressResponse> getDressById(@PathVariable Long id) {
        DressResponse dress = dressService.getDressById(id);
        return ResponseEntity.ok(dress);
    }

    // POST /api/dress - 새 드레스 생성
    @PostMapping
    public ResponseEntity<DressResponse> createDress(@RequestBody DressRequest request) {
        DressResponse dress = dressService.createDress(request);
        return ResponseEntity.ok(dress);
    }

    // PUT /api/dress/{id} - 드레스 정보 수정
    @PutMapping("/{id}")
    public ResponseEntity<DressResponse> updateDress(
            @PathVariable Long id,
            @RequestBody DressRequest request) {
        DressResponse dress = dressService.updateDress(id, request);
        return ResponseEntity.ok(dress);
    }

    // DELETE /api/dress/{id} - 드레스 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDress(@PathVariable Long id) {
        dressService.deleteDress(id);
        return ResponseEntity.noContent().build();
    }
}