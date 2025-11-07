package com.swyp.wedding.controller.dress;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.swyp.wedding.dto.dress.DressRequest;
import com.swyp.wedding.dto.dress.DressResponse;
import com.swyp.wedding.entity.common.SortType;
import com.swyp.wedding.service.dress.DressService;

import java.util.List;

@Tag(name = "Dress", description = "웨딩 드레스 관리 API")
@RestController
@RequestMapping("/api/dress")
@RequiredArgsConstructor
public class DressController {

    private final DressService dressService;

    @Operation(summary = "드레스 목록 조회", 
               description = "드레스 목록을 조회합니다. 파라미터로 검색 및 정렬 옵션을 지정할 수 있습니다. 여러 조건을 동시에 사용 가능합니다.")
    @GetMapping
    public ResponseEntity<List<DressResponse>> getAllDresses(
            @Parameter(description = "샵 이름 (부분 일치 검색)") @RequestParam(required = false) String shopNameContains,
            @Parameter(description = "정렬 기준: RECENT(최신순), FAVORITE(인기순)", example = "RECENT") 
            @RequestParam(required = false) SortType sort) {
        
        // 복합 조건으로 검색 (null이 아닌 모든 파라미터 적용)
        List<DressResponse> dresses = dressService.searchDresses(shopNameContains, sort);
        
        return ResponseEntity.ok(dresses);
    }

    @Operation(summary = "특정 드레스 조회", description = "ID로 특정 드레스의 상세 정보를 조회합니다.")
    @GetMapping("/{id}")
    public ResponseEntity<DressResponse> getDressById(
            @Parameter(description = "드레스 ID", required = true) @PathVariable Long id) {
        DressResponse dress = dressService.getDressById(id);
        return ResponseEntity.ok(dress);
    }

    @Operation(summary = "새 드레스 등록", description = "새로운 드레스를 등록합니다. shop_name은 필수입니다.")
    @PostMapping
    public ResponseEntity<DressResponse> createDress(@RequestBody DressRequest request) {
        DressResponse dress = dressService.createDress(request);
        return ResponseEntity.ok(dress);
    }

    @Operation(summary = "드레스 정보 수정", description = "기존 드레스의 정보를 수정합니다.")
    @PutMapping("/{id}")
    public ResponseEntity<DressResponse> updateDress(
            @Parameter(description = "드레스 ID", required = true) @PathVariable Long id,
            @RequestBody DressRequest request) {
        DressResponse dress = dressService.updateDress(id, request);
        return ResponseEntity.ok(dress);
    }

    @Operation(summary = "드레스 삭제", description = "드레스를 삭제합니다.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDress(
            @Parameter(description = "드레스 ID", required = true) @PathVariable Long id) {
        dressService.deleteDress(id);
        return ResponseEntity.noContent().build();
    }
}