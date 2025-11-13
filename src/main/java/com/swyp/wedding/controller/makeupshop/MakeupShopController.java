package com.swyp.wedding.controller.makeupshop;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.swyp.wedding.dto.makeupshop.MakeupShopRequest;
import com.swyp.wedding.dto.makeupshop.MakeupShopResponse;
import com.swyp.wedding.entity.common.SortType;
import com.swyp.wedding.global.response.ApiResponse;
import com.swyp.wedding.security.user.CustomUserDetails;
import com.swyp.wedding.service.makeupshop.MakeupShopService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "메이크업샵", description = "웨딩 메이크업 샵 관리 API")
@RestController
@RequestMapping("/api/makeup-shop")
@RequiredArgsConstructor
public class MakeupShopController {

    private final MakeupShopService makeupShopService;

    @Operation(summary = "메이크업샵 목록 조회",
               description = "메이크업샵 목록을 조회합니다. 파라미터로 검색 및 정렬 옵션을 지정할 수 있습니다. 여러 조건을 동시에 사용 가능합니다. 로그인 시 찜 정보(isLiked)가 포함됩니다.")
    @GetMapping
    public ResponseEntity<ApiResponse<List<MakeupShopResponse>>> getAllMakeupShops(
            @Parameter(description = "샵 이름 (부분 일치 검색)") @RequestParam(required = false) String shopName,
            @Parameter(description = "주소/지역 (부분 일치 검색)") @RequestParam(required = false) String address,
            @Parameter(description = "전문분야 (부분 일치 검색)") @RequestParam(required = false) String specialty,
            @Parameter(description = "정렬 기준: RECENT(최신순), FAVORITE(인기순)", example = "RECENT")
            @RequestParam(required = false) SortType sort,
            @AuthenticationPrincipal(errorOnInvalidType = false) CustomUserDetails userDetails) {

        // 로그인 여부 확인
        String userId = (userDetails != null) ? userDetails.getUsername() : null;

        // 복합 조건으로 검색 (null이 아닌 모든 파라미터 적용)
        List<MakeupShopResponse> makeupShops = makeupShopService.searchMakeupShops(shopName, address, specialty, sort, userId);

        return ResponseEntity.ok(ApiResponse.success(makeupShops));
    }

    @Operation(summary = "특정 메이크업샵 조회", description = "ID로 특정 메이크업샵의 상세 정보를 조회합니다. 로그인 시 찜 정보(isLiked)가 포함됩니다.")
    @GetMapping("/{id}")
<<<<<<< HEAD
    public ResponseEntity<ApiResponse<MakeupShopResponse>> getMakeupShopById(
=======
    public ResponseEntity<MakeupShopResponse> getMakeupShopById(
>>>>>>> ef26183 (feat: 상세보기 api 찜여부 추가)
            @Parameter(description = "메이크업샵 ID", required = true) @PathVariable Long id,
            @AuthenticationPrincipal(errorOnInvalidType = false) CustomUserDetails userDetails) {

        // 로그인 여부 확인
        String userId = (userDetails != null) ? userDetails.getUsername() : null;

        MakeupShopResponse makeupShop = makeupShopService.getMakeupShopById(id, userId);
<<<<<<< HEAD
        return ResponseEntity.ok(ApiResponse.success(makeupShop));
=======
        return ResponseEntity.ok(makeupShop);
>>>>>>> ef26183 (feat: 상세보기 api 찜여부 추가)
    }

    @Operation(summary = "새 메이크업샵 생성", description = "새로운 메이크업샵을 등록합니다.")
    @PostMapping
    public ResponseEntity<ApiResponse<MakeupShopResponse>> createMakeupShop(@RequestBody MakeupShopRequest request) {
        MakeupShopResponse makeupShop = makeupShopService.createMakeupShop(request);
        return ResponseEntity.ok(ApiResponse.success(makeupShop));
    }

    @Operation(summary = "메이크업샵 정보 수정", description = "기존 메이크업샵의 정보를 수정합니다.")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<MakeupShopResponse>> updateMakeupShop(
            @Parameter(description = "메이크업샵 ID", required = true) @PathVariable Long id,
            @RequestBody MakeupShopRequest request) {
        MakeupShopResponse makeupShop = makeupShopService.updateMakeupShop(id, request);
        return ResponseEntity.ok(ApiResponse.success(makeupShop));
    }

    @Operation(summary = "메이크업샵 삭제", description = "메이크업샵을 삭제합니다.")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteMakeupShop(
            @Parameter(description = "메이크업샵 ID", required = true) @PathVariable Long id) {
        makeupShopService.deleteMakeupShop(id);
        return ResponseEntity.ok(ApiResponse.success());
    }
}
