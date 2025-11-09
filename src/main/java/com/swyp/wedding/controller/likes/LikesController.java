package com.swyp.wedding.controller.likes;

import com.swyp.wedding.security.user.CustomUserDetails;
import com.swyp.wedding.service.likes.impl.LikesServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Map;

@Tag(name = "찜 기능 API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/likes")
public class LikesController {

    private final LikesServiceImpl likesService;

    @Operation(summary = "사용자가 특정 게시물 좋아요(찜)를 누릅니다.")
    @PostMapping("/{category}/{postId}")
    public ResponseEntity<Map<String, Object>> storeLikes(@PathVariable String category, @PathVariable Long postId,
                                                          @AuthenticationPrincipal Principal principal) {
        // 임의 유저 id 설정
        String userId = principal.getName();

        return ResponseEntity.ok(Map.of("result", likesService.storeLikes(category, postId, userId)));
    }
}
