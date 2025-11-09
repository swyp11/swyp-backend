package com.swyp.wedding.dto.dress;

import lombok.Data;
import com.swyp.wedding.entity.dress.Dress;
import com.swyp.wedding.entity.dress.DressEnum;

@Data
public class DressRequest {
    private Long id;
    private String name;      
    private String color;           
    private String shape;           
    private String priceRange;      
    private DressEnum.Length length;        
    private DressEnum.Season season;   
    private String shopName;        // DressShop의 shopName (필수)
    private String designer;        
    private DressEnum.Type type;            
    private DressEnum.Neckline neckLine;     
    private DressEnum.Mood mood;              
    private String fabric;  // 여러 원단 타입을 콤마로 구분 (예: "LACE,BEADS")
    private String features;  // 특징 또는 기타 컬럼            

    public Dress toEntity() {
        return Dress.builder()
                .name(name)                 
                .color(color)
                .shape(shape)
                .priceRange(priceRange)
                .length(length)
                .season(season)
                .shopName(shopName)             
                .designer(designer)
                .type(type)              
                .neckLine(neckLine)       
                .mood(mood)             
                .fabric(fabric)
                .features(features)         
                .build();
    }
}