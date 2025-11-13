package com.swyp.wedding.controller.dressshop;

import com.swyp.wedding.global.response.ApiResponse;
import com.swyp.wedding.security.user.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.swyp.wedding.dto.dressshop.DressShopRequest;
import com.swyp.wedding.dto.dressshop.DressShopResponse;
import com.swyp.wedding.entity.common.SortType;
import com.swyp.wedding.service.dressshop.DressShopService;
import com.swyp.wedding.service.dress.DressService;

import java.util.List;

@Tag(name = "드레스샵", description = "웨딩 드레스 샵 관리 API")
@RestController
@RequestMapping("/api/dress-shop")
@RequiredArgsConstructor
public class DressShopController {

    private final DressShopService dressShopService;
    private final DressService dressService;

    @Operation(summary = "드레스샵 목록 조회",
               description = "드레스샵 목록을 조회합니다. 파라미터로 검색 및 정렬 옵션을 지정할 수 있습니다. 여러 조건을 동시에 사용 가능합니다. 로그인 시 찜 정보(isLiked)가 포함됩니다.")
    @GetMapping
    public ResponseEntity<ApiResponse<List<DressShopResponse>>> getAllDressShops(
            @Parameter(description = "샵 이름 (부분 일치 검색)") @RequestParam(required = false) String shopName,
            @Parameter(description = "주소/지역 (부분 일치 검색)") @RequestParam(required = false) String address,
            @Parameter(description = "전문분야 (부분 일치 검색)") @RequestParam(required = false) String specialty,
            @Parameter(description = "정렬 기준: RECENT(최신순), FAVORITE(인기순)", example = "RECENT")
            @RequestParam(required = false) SortType sort,
            @AuthenticationPrincipal(errorOnInvalidType = false) CustomUserDetails userDetails) {

        // 로그인 여부 확인
        String userId = (userDetails != null) ? userDetails.getUsername() : null;

        // 복합 조건으로 검색 (null이 아닌 모든 파라미터 적용)
        List<DressShopResponse> dressShops = dressShopService.searchDressShops(shopName, address, specialty, sort, userId);

        return ResponseEntity.ok(ApiResponse.success(dressShops));
    }

    @Operation(summary = "특정 드레스샵 조회", description = "ID로 특정 드레스샵의 상세 정보를 조회합니다. 로그인 시 찜 정보(isLiked)가 포함됩니다.")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<DressShopResponse>> getDressShopById(
            @Parameter(description = "드레스샵 ID", required = true) @PathVariable Long id,
            @AuthenticationPrincipal(errorOnInvalidType = false) CustomUserDetails userDetails) {
        String userId = (userDetails != null) ? userDetails.getUsername() : null;
        DressShopResponse dressShop = dressShopService.getDressShopById(id, userId);
        return ResponseEntity.ok(ApiResponse.success(dressShop));
    }

    @Operation(summary = "새 드레스샵 생성", description = "새로운 드레스샵을 등록합니다.")
    @PostMapping
    public ResponseEntity<ApiResponse<DressShopResponse>> createDressShop(@RequestBody DressShopRequest request) {
        DressShopResponse dressShop = dressShopService.createDressShop(request);
        return ResponseEntity.ok(ApiResponse.success(dressShop));
    }

    @Operation(summary = "드레스샵 정보 수정", description = "기존 드레스샵의 정보를 수정합니다.")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<DressShopResponse>> updateDressShop(
            @Parameter(description = "드레스샵 ID", required = true) @PathVariable Long id,
            @RequestBody DressShopRequest request) {
        DressShopResponse dressShop = dressShopService.updateDressShop(id, request);
        return ResponseEntity.ok(ApiResponse.success(dressShop));
    }

    @Operation(summary = "드레스샵 삭제", description = "드레스샵을 삭제합니다.")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteDressShop(
            @Parameter(description = "드레스샵 ID", required = true) @PathVariable Long id) {
        dressShopService.deleteDressShop(id);
        return ResponseEntity.ok(ApiResponse.success());
    }

    @Operation(summary = "샵의 드레스 목록 조회", description = "특정 드레스샵에 등록된 모든 드레스를 조회합니다.")
    @GetMapping("/{id}/dresses")
    public ResponseEntity<ApiResponse<List<com.swyp.wedding.dto.dress.DressResponse>>> getDressesByShop(
            @Parameter(description = "드레스샵 ID", required = true) @PathVariable Long id) {
        // 1. 먼저 해당 ID의 DressShop을 조회하여 shop_name을 가져옴 (찜 정보는 불필요하므로 null 전달)
        DressShopResponse dressShop = dressShopService.getDressShopById(id, null);

        // 2. shop_name으로 Dress 목록 조회
        List<com.swyp.wedding.dto.dress.DressResponse> dresses =
            dressService.getDressesByShopName(dressShop.getShopName());

        return ResponseEntity.ok(ApiResponse.success(dresses));
    }
}