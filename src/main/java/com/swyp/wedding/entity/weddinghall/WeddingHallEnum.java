package com.swyp.wedding.entity.weddinghall;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum WeddingHallEnum {
    HOTEL(0, "호텔"),
    WEDDING_HALL(1, "웨딩홀"),
    OUTDOOR(2,"야외"),
    RESTAURANT(3, "레스토랑"),
    HOUSE_STUDIO(4, "하우스스튜디오"),
    GARDEN(5, "가든"),
    OTHER(6, "기타");

    private Integer id;
    private String title;
}
