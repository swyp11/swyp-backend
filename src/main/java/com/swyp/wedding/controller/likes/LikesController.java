package com.swyp.wedding.controller.likes;

import com.swyp.wedding.global.response.ApiResponse;
import com.swyp.wedding.service.likes.impl.LikesServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Tag(name = "찜 기능 API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/likes")
public class LikesController {

    private final LikesServiceImpl likesService;

    @Operation(summary = "사용자가 특정 게시물 좋아요(찜)를 누릅니다.")
    @PostMapping("/{category}/{postId}")
    public ResponseEntity<ApiResponse<Void>> storeLikes(@PathVariable String category, @PathVariable Long postId,
                                                          @AuthenticationPrincipal Principal principal) {
        String userId = principal.getName();
        likesService.storeLikes(category, postId, userId);
        return ResponseEntity.ok(ApiResponse.success());
    }

    @Operation(summary = "사용자가 특정 게시물 좋아요(찜)를 제거합니다.")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteLikes(@PathVariable Long id,
                                                          @AuthenticationPrincipal Principal principal) {
        // 추후 계정 없는 것에 대한 예외처리 기능 만들 예정 (고도화 때)
        likesService.deleteLikes(id);
        return ResponseEntity.ok(ApiResponse.success());
    }
}
