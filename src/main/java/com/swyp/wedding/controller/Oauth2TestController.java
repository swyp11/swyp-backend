package com.swyp.wedding.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Oauth2TestController {
    // 소셜 로그인 성공 후 리다이렉션 부분
    @GetMapping("/home")
    public String home() {
        return "google social";
    }

    @GetMapping("/")
    public String main() {
        return "main page";
    }

    @GetMapping("/user")
    public String userpage() {
        return "User 권한 확인 페이지";
    }


}
