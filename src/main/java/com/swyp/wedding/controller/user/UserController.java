package com.swyp.wedding.controller.user;

import com.swyp.wedding.dto.user.UserRequest;
import com.swyp.wedding.service.AuthService;
import com.swyp.wedding.service.JoinService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequiredArgsConstructor
public class UserController {

    private final JoinService joinService;
    private final AuthService authService;

    @PostMapping("/join")
    public String JoinProcess(@RequestBody UserRequest userRequest){
        return joinService.JoinProcess(userRequest);
    }


}
