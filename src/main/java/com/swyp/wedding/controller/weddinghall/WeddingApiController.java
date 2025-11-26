package com.swyp.wedding.controller.weddinghall;

import java.util.List;

import com.swyp.wedding.dto.dress.DressResponse;
import com.swyp.wedding.dto.hall.HallResponse;
import com.swyp.wedding.global.response.ApiResponse;
import com.swyp.wedding.security.user.CustomUserDetails;
import com.swyp.wedding.service.hall.impl.HallServiceImpl;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.swyp.wedding.dto.weddinghall.WeddingHallRequest;
import com.swyp.wedding.dto.weddinghall.WeddingHallResponse;
import com.swyp.wedding.entity.common.SortType;
import com.swyp.wedding.service.weddinghall.impl.WeddingHallServiceImpl;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

@Tag(name = "웨딩홀", description = "웨딩홀 관리 API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/wedding")
public class WeddingApiController {

    private final WeddingHallServiceImpl weddingHallService;
    private final HallServiceImpl hallService;

    @Operation(summary = "웨딩홀 리스트를 조회합니다.",
               description = "정렬 기준: RECENT(최신순), FAVORITE(인기순). 기본값은 최신순입니다. 로그인 시 찜 정보(isLiked)가 포함됩니다.")
    @GetMapping
    public ResponseEntity<ApiResponse<List<WeddingHallResponse>>> getWeddings(
            @Parameter(description = "정렬 기준: RECENT(최신순), FAVORITE(인기순)", example = "RECENT")
            @RequestParam(required = false) SortType sort,
            @AuthenticationPrincipal(errorOnInvalidType = false) CustomUserDetails userDetails) {

        // 로그인 여부 확인
        String userId = (userDetails != null) ? userDetails.getUsername() : null;

        return ResponseEntity.ok(ApiResponse.success(weddingHallService.getWeddingInfos(sort, userId)));
    }

    @Operation(summary = "웨딩홀에 대한 상세정보를 조회합니다.", description = "로그인 시 찜 정보(isLiked)가 포함됩니다.")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<WeddingHallResponse>> getWeddingInfo(
            @PathVariable("id") Long id,
            @AuthenticationPrincipal(errorOnInvalidType = false) CustomUserDetails userDetails) {

        // 로그인 여부 확인
        String userId = (userDetails != null) ? userDetails.getUsername() : null;

        return ResponseEntity.ok(ApiResponse.success(weddingHallService.getWeddingInfo(id, userId)));
    }

    @Operation(summary = "웨딩홀에 대한 정보를 저장합니다.")
    @PostMapping
    public ResponseEntity<ApiResponse<Void>> saveWedding(@RequestBody WeddingHallRequest request) {
        weddingHallService.saveWedding(request);
        return ResponseEntity.ok(ApiResponse.success());
    }

    @Operation(summary = "웨딩홀에 대한 정보를 수정합니다.")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> updateWedding(@PathVariable Long id, @RequestBody WeddingHallRequest request) {
        request.setId(id);
        weddingHallService.updateWedding(request);
        return ResponseEntity.ok(ApiResponse.success());
    }

    @Operation(summary = "웨딩홀에 대한 정보를 삭제합니다.")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteWedding(@PathVariable Long id) {
        weddingHallService.deleteWedding(id);
        return ResponseEntity.ok(ApiResponse.success());
    }

    @Operation(summary = "웨딩홀 키워드 검색을 합니다.")
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<WeddingHallResponse>>> searchWeddings(@RequestParam String keyword) {
        List<WeddingHallResponse> results = weddingHallService.searchWeddings(keyword);
        return ResponseEntity.ok(ApiResponse.success(results));
    }

    @Operation(summary = "웨딩홀의 홀 상세 목록 조회", description = "웨딩홀에 등록된 홀의 상세 정보를 조회합니다.")
    @GetMapping("/{weddingHallId}/halls")
    public ResponseEntity<ApiResponse<List<HallResponse>>> getHallsByWeddingHall(@PathVariable Long weddingHallId) {
        List<HallResponse> hallLists = hallService.getHallByWeddingHallId(weddingHallId);
        return ResponseEntity.ok(ApiResponse.success(hallLists));
    }
}
