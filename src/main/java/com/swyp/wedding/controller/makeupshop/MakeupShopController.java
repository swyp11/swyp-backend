package com.swyp.wedding.controller.makeupshop;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.swyp.wedding.dto.makeupshop.MakeupShopRequest;
import com.swyp.wedding.dto.makeupshop.MakeupShopResponse;
import com.swyp.wedding.entity.common.SortType;
import com.swyp.wedding.service.makeupshop.MakeupShopService;

import java.util.List;

@Tag(name = "메이크업샵", description = "웨딩 메이크업 샵 관리 API")
@RestController
@RequestMapping("/api/makeup-shop")
@RequiredArgsConstructor
public class MakeupShopController {

    private final MakeupShopService makeupShopService;

    @Operation(summary = "메이크업샵 목록 조회", 
               description = "메이크업샵 목록을 조회합니다. 파라미터로 검색 및 정렬 옵션을 지정할 수 있습니다. 여러 조건을 동시에 사용 가능합니다.")
    @GetMapping
    public ResponseEntity<List<MakeupShopResponse>> getAllMakeupShops(
            @Parameter(description = "샵 이름 (부분 일치 검색)") @RequestParam(required = false) String shopName,
            @Parameter(description = "주소/지역 (부분 일치 검색)") @RequestParam(required = false) String address,
            @Parameter(description = "전문분야 (부분 일치 검색)") @RequestParam(required = false) String specialty,
            @Parameter(description = "정렬 기준: RECENT(최신순)", example = "RECENT") 
            @RequestParam(required = false) SortType sort) {
        
        // 복합 조건으로 검색 (null이 아닌 모든 파라미터 적용)
        List<MakeupShopResponse> makeupShops = makeupShopService.searchMakeupShops(shopName, address, specialty, sort);
        
        return ResponseEntity.ok(makeupShops);
    }

    @Operation(summary = "특정 메이크업샵 조회", description = "ID로 특정 메이크업샵의 상세 정보를 조회합니다.")
    @GetMapping("/{id}")
    public ResponseEntity<MakeupShopResponse> getMakeupShopById(
            @Parameter(description = "메이크업샵 ID", required = true) @PathVariable Long id) {
        MakeupShopResponse makeupShop = makeupShopService.getMakeupShopById(id);
        return ResponseEntity.ok(makeupShop);
    }

    @Operation(summary = "새 메이크업샵 생성", description = "새로운 메이크업샵을 등록합니다.")
    @PostMapping
    public ResponseEntity<MakeupShopResponse> createMakeupShop(@RequestBody MakeupShopRequest request) {
        MakeupShopResponse makeupShop = makeupShopService.createMakeupShop(request);
        return ResponseEntity.ok(makeupShop);
    }

    @Operation(summary = "메이크업샵 정보 수정", description = "기존 메이크업샵의 정보를 수정합니다.")
    @PutMapping("/{id}")
    public ResponseEntity<MakeupShopResponse> updateMakeupShop(
            @Parameter(description = "메이크업샵 ID", required = true) @PathVariable Long id,
            @RequestBody MakeupShopRequest request) {
        MakeupShopResponse makeupShop = makeupShopService.updateMakeupShop(id, request);
        return ResponseEntity.ok(makeupShop);
    }

    @Operation(summary = "메이크업샵 삭제", description = "메이크업샵을 삭제합니다.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMakeupShop(
            @Parameter(description = "메이크업샵 ID", required = true) @PathVariable Long id) {
        makeupShopService.deleteMakeupShop(id);
        return ResponseEntity.noContent().build();
    }
}
