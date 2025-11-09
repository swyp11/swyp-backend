package com.swyp.wedding.entity.hall;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum LightType {
    BRIGHT(0, "밝은 조명"),
    DIM(1, "어두운 조명"),
    NATURAL(2,"자연광 조명");

    private Integer id;
    private String title;
}
