package com.swyp.wedding.entity.likes;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum LikesType {
    HALL("홀"),
    WEDDING_HALL("웨딩홀"),
    DRESS("드레스"),
    DRESS_SHOP("드레스샵"),
    MAKEUP_SHOP("메이크업샵");

    private final String description;
}
