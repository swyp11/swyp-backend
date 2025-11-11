package com.swyp.wedding.dto.dress;

import lombok.Data;
import com.swyp.wedding.entity.dress.Dress;
import com.swyp.wedding.entity.dress.DressEnum;

import java.time.LocalDateTime;

@Data
public class DressResponse {
    private Long id;
    private String name;                        
    private String color;                      
    private String shape;                      
    private String priceRange;                 
    private DressEnum.Length length;          
    private DressEnum.Season season;           
    private String shopName;                    // DressShop 이름 (브랜드명)
    private String designer;                    
    private DressEnum.Type type;               
    private DressEnum.Neckline neckLine;        
    private DressEnum.Mood mood;
    private String fabric;  // 여러 원단 타입을 콤마로 구분
    private String imageUrl;  // image URL
    private String features;  // 특징 또는 기타 컬럼
    private LocalDateTime regDt;
    private LocalDateTime updateDt;

    public static DressResponse from(Dress dress) {
        DressResponse response = new DressResponse();
        response.setId(dress.getId());
        response.setName(dress.getName());
        response.setColor(dress.getColor());                
        response.setShape(dress.getShape()); 
        response.setPriceRange(dress.getPriceRange()); 
        response.setLength(dress.getLength());             
        response.setSeason(dress.getSeason());
        response.setShopName(dress.getShopName());               
        response.setDesigner(dress.getDesigner());         
        response.setType(dress.getType());                 
        response.setNeckLine(dress.getNeckLine());    
        response.setMood(dress.getMood());
        response.setFabric(dress.getFabric());
        response.setImageUrl(dress.getImageUrl());
        response.setFeatures(dress.getFeatures());
        response.setRegDt(dress.getRegDt());              
        response.setUpdateDt(dress.getUpdateDt());        
        return response;
    }
}