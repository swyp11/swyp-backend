package com.swyp.wedding.controller.dressshop;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.swyp.wedding.dto.dressshop.DressShopRequest;
import com.swyp.wedding.dto.dressshop.DressShopResponse;
import com.swyp.wedding.service.dressshop.DressShopService;
import com.swyp.wedding.service.dress.DressService;

import java.util.List;

@Tag(name = "DressShop", description = "웨딩 드레스 샵 관리 API")
@RestController
@RequestMapping("/api/dress-shop")
@RequiredArgsConstructor
public class DressShopController {

    private final DressShopService dressShopService;
    private final DressService dressService;

    @Operation(summary = "전체 드레스샵 목록 조회", description = "등록된 모든 드레스샵의 목록을 조회합니다.")
    @GetMapping
    public ResponseEntity<List<DressShopResponse>> getAllDressShops() {
        List<DressShopResponse> dressShops = dressShopService.getAllDressShops();
        return ResponseEntity.ok(dressShops);
    }

    @Operation(summary = "특정 드레스샵 조회", description = "ID로 특정 드레스샵의 상세 정보를 조회합니다.")
    @GetMapping("/{id}")
    public ResponseEntity<DressShopResponse> getDressShopById(
            @Parameter(description = "드레스샵 ID", required = true) @PathVariable Long id) {
        DressShopResponse dressShop = dressShopService.getDressShopById(id);
        return ResponseEntity.ok(dressShop);
    }

    @Operation(summary = "새 드레스샵 생성", description = "새로운 드레스샵을 등록합니다.")
    @PostMapping
    public ResponseEntity<DressShopResponse> createDressShop(@RequestBody DressShopRequest request) {
        DressShopResponse dressShop = dressShopService.createDressShop(request);
        return ResponseEntity.ok(dressShop);
    }

    @Operation(summary = "드레스샵 정보 수정", description = "기존 드레스샵의 정보를 수정합니다.")
    @PutMapping("/{id}")
    public ResponseEntity<DressShopResponse> updateDressShop(
            @Parameter(description = "드레스샵 ID", required = true) @PathVariable Long id,
            @RequestBody DressShopRequest request) {
        DressShopResponse dressShop = dressShopService.updateDressShop(id, request);
        return ResponseEntity.ok(dressShop);
    }

    @Operation(summary = "드레스샵 삭제", description = "드레스샵을 삭제합니다.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDressShop(
            @Parameter(description = "드레스샵 ID", required = true) @PathVariable Long id) {
        dressShopService.deleteDressShop(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "샵 이름으로 검색", description = "샵 이름으로 드레스샵을 검색합니다. (부분 일치)")
    @GetMapping("/search/name")
    public ResponseEntity<List<DressShopResponse>> searchByShopName(
            @Parameter(description = "검색할 샵 이름", required = true) @RequestParam String shopName) {
        List<DressShopResponse> dressShops = dressShopService.searchByShopName(shopName);
        return ResponseEntity.ok(dressShops);
    }

    @Operation(summary = "지역으로 검색", description = "주소를 기준으로 드레스샵을 검색합니다. (부분 일치)")
    @GetMapping("/search/address")
    public ResponseEntity<List<DressShopResponse>> searchByAddress(
            @Parameter(description = "검색할 지역/주소", required = true) @RequestParam String address) {
        List<DressShopResponse> dressShops = dressShopService.searchByAddress(address);
        return ResponseEntity.ok(dressShops);
    }

    @Operation(summary = "전문분야로 검색", description = "전문분야(specialty)로 드레스샵을 검색합니다. 예: 가성비, 프리미엄, 빅사이즈")
    @GetMapping("/search/specialty")
    public ResponseEntity<List<DressShopResponse>> searchBySpecialty(
            @Parameter(description = "검색할 전문분야", required = true) @RequestParam String specialty) {
        List<DressShopResponse> dressShops = dressShopService.searchBySpecialty(specialty);
        return ResponseEntity.ok(dressShops);
    }
    
    @Operation(summary = "샵의 드레스 목록 조회", description = "특정 드레스샵에 등록된 모든 드레스를 조회합니다.")
    @GetMapping("/{id}/dresses")
    public ResponseEntity<List<com.swyp.wedding.dto.dress.DressResponse>> getDressesByShop(
            @Parameter(description = "드레스샵 ID", required = true) @PathVariable Long id) {
        // 1. 먼저 해당 ID의 DressShop을 조회하여 shop_name을 가져옴
        DressShopResponse dressShop = dressShopService.getDressShopById(id);
        
        // 2. shop_name으로 Dress 목록 조회
        List<com.swyp.wedding.dto.dress.DressResponse> dresses = 
            dressService.getDressesByShopName(dressShop.getShopName());
        
        return ResponseEntity.ok(dresses);
    }
    
    @Operation(summary = "최신 드레스샵 목록 조회", 
               description = "등록일(reg_dt) 기준으로 최신순으로 정렬된 드레스샵 목록을 조회합니다.")
    @GetMapping("/recent")
    public ResponseEntity<List<DressShopResponse>> getRecentDressShops() {
        List<DressShopResponse> dressShops = dressShopService.getAllDressShopsOrderByNewest();
        return ResponseEntity.ok(dressShops);
    }
}