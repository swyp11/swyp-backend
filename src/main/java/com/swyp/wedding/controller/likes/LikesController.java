package com.swyp.wedding.controller.likes;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.swyp.wedding.dto.likes.LikesResponse;
import com.swyp.wedding.global.response.ApiResponse;
import com.swyp.wedding.security.user.CustomUserDetails;
import com.swyp.wedding.service.likes.impl.LikesServiceImpl;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "찜", description = "찜 기능 관리 API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/likes")
public class LikesController {

    private final LikesServiceImpl likesService;

    @Operation(summary = "사용자가 특정 게시물 좋아요(찜)를 누릅니다.")
    @PostMapping("/{category}/{postId}")
    public ResponseEntity<ApiResponse<Void>> storeLikes(@PathVariable String category, @PathVariable Long postId,
                                                          @AuthenticationPrincipal CustomUserDetails userDetails) {
        String userId = userDetails.getUsername();
        likesService.storeLikes(category, postId, userId);
        return ResponseEntity.ok(ApiResponse.success());
    }

    @Operation(summary = "사용자가 특정 게시물 좋아요(찜)를 제거합니다. (category, postId 기반)")
    @DeleteMapping("/{category}/{postId}")
    public ResponseEntity<ApiResponse<Void>> deleteLikesByPost(@PathVariable String category,
                                                                @PathVariable Long postId,
                                                                @AuthenticationPrincipal CustomUserDetails userDetails) {
        String userId = userDetails.getUsername();
        likesService.deleteLikesByPost(category, postId, userId);
        return ResponseEntity.ok(ApiResponse.success());
    }

    @Operation(summary = "사용자의 모든 찜 목록 조회",
               description = "로그인한 사용자의 모든 찜 목록을 최신순으로 조회합니다. 각 아이템의 상세 정보가 포함됩니다.")
    @GetMapping
    public ResponseEntity<ApiResponse<List<LikesResponse>>> getUserLikes(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        String userId = userDetails.getUsername();
        List<LikesResponse> likes = likesService.getUserLikes(userId);
        return ResponseEntity.ok(ApiResponse.success(likes));
    }

    @Operation(summary = "사용자의 카테고리별 찜 목록 조회",
               description = "로그인한 사용자의 특정 카테고리 찜 목록을 최신순으로 조회합니다. category: hall, wedding_hall, dress, shop")
    @GetMapping("/category/{category}")
    public ResponseEntity<ApiResponse<List<LikesResponse>>> getUserLikesByCategory(
            @Parameter(description = "카테고리: hall, wedding_hall, dress, shop", required = true)
            @PathVariable String category,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        String userId = userDetails.getUsername();
        List<LikesResponse> likes = likesService.getUserLikesByCategory(userId, category);
        return ResponseEntity.ok(ApiResponse.success(likes));
    }
}
