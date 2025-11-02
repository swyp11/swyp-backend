package com.swyp.wedding.entity.hall;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum HallType {
    SINGLE(0, "단독 홀"),
    COMPLEX(1, "복합 홀"),
    CONVENTION(2,"컨벤션 홀");

    private Integer id;
    private String title;
}
