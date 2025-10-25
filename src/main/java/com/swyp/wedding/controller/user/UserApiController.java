package com.swyp.wedding.controller.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
@Tag(name = "사용자 정보에 관한 api")
public class UserApiController {

    @Operation(summary = "테스트", description = "스웨거 정상 작동 테스트")
    @GetMapping("test")
    public String test() {
        return "test";
    }
}
