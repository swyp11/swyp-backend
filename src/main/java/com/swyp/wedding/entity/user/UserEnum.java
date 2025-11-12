package com.swyp.wedding.entity.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum UserEnum {
    USER(0, "유저", "서비스 사용 유저"),
    ADMIN(1, "관리자", "서비스 관리자");

    private Integer id;
    private String title;
    private String desc;
}
