package com.swyp.wedding.controller.join;

import com.swyp.wedding.dto.user.UserRequest;
import com.swyp.wedding.service.JoinService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class JoinController {

    private final JoinService joinService;

    @PostMapping("/join")
    public String JoinProcess(@RequestBody UserRequest userRequest){
        return joinService.JoinProcess(userRequest);
    }
}
