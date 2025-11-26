package com.swyp.wedding.dto.dress;

import lombok.Data;
import com.swyp.wedding.entity.dress.Dress;
import com.swyp.wedding.entity.dress.DressEnum;
import com.swyp.wedding.entity.dressshop.DressShop;

@Data
public class DressRequest {
    private Long id;
    private String name;
    private String color;
    private String shape;
    private String priceRange;
    private DressEnum.Length length;
    private DressEnum.Season season;
    private Long dressShopId;       // DressShop ID (필수)
    private String designer;
    private DressEnum.Type type;
    private DressEnum.Neckline neckLine;
    private DressEnum.Mood mood;
    private String fabric;  // 여러 원단 타입을 콤마로 구분 (예: "LACE,BEADS")
    private String imageUrl;  // image URL
    private String features;  // 특징 또는 기타 컬럼

    public Dress toEntity(DressShop dressShop) {
        return Dress.builder()
                .name(name)
                .color(color)
                .shape(shape)
                .priceRange(priceRange)
                .length(length)
                .season(season)
                .dressShop(dressShop)
                .designer(designer)
                .type(type)
                .neckLine(neckLine)
                .mood(mood)
                .fabric(fabric)
                .imageUrl(imageUrl)
                .features(features)
                .build();
    }
}