package com.swyp.wedding.dto.auth;

import lombok.Getter;

@Getter
public class EmailAuthRequest {
    private String email;
    private String code;
}
